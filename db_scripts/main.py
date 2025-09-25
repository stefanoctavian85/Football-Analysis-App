import json
import psycopg2
from dotenv import load_dotenv
import os
import pandas as pd

pd.set_option("display.max_columns", 10)

load_dotenv()

database_name = os.getenv('POSTGRE_DB_NAME')
database_user = os.getenv('POSTGRE_USER')
database_password = os.getenv('POSTGRE_PASSWORD')
database_host = os.getenv('POSTGRE_HOST')
database_port = os.getenv('POSTGRE_PORT')

connection = psycopg2.connect(database=database_name, user=database_user, password=database_password,
                              host=database_host,
                              port=database_port)
cursor = connection.cursor()


def insert_competitions():
    competitions = pd.read_json("data/competitions.json")
    competitions_db = pd.DataFrame({
        "competition_id": competitions["competition_id"],
        "season_id": competitions["season_id"],
        "country_name": competitions["country_name"],
        "competition_name": competitions["competition_name"],
        "competition_gender": competitions["competition_gender"],
        "season_name": competitions["season_name"],
    })
    competitions_db.sort_values(ascending=True, by="competition_id", inplace=True)
    for _, row in competitions_db.iterrows():
        cursor.execute("INSERT INTO COMPETITIONS(competition_id, season_id, country_name, competition_name, "
                       "competition_gender, season_name)"
                       "VALUES (%s, %s, %s, %s, %s, %s)"
                       "ON CONFLICT (competition_id, season_id) DO NOTHING;", tuple(row))

    connection.commit()
    return competitions_db


def insert_matches():
    base_url = "data/matches"

    for _, row in competitions.iterrows():
        competition_id = row["competition_id"]
        season_id = row["season_id"]

        file_path = f"{base_url}/{competition_id}/{season_id}.json"
        if not os.path.exists(file_path):
            continue

        with open(file_path, "r", encoding="utf-8") as f:
            matches = json.load(f)

        for match in matches:
            insert_match(match)


def insert_match(match):
    ref = match.get("referee")
    if ref:
        country = ref.get("country", {})
        safe_insert("countries", ["country_id", "country_name"], {
            "country_id": country.get("id"),
            "country_name": country.get("name")
        })

        safe_insert("referees", ["referee_id", "referee_name", "country_id"], {
            "referee_id": ref.get("id"),
            "referee_name": ref.get("name"),
            "country_id": country.get("id"),
        })

    stadiums = match.get("stadium")
    if stadiums:
        country = stadiums.get("country", {})
        safe_insert("countries", ["country_id", "country_name"], {
            "country_id": country.get("id"),
            "country_name": country.get("name")
        })

        safe_insert("stadiums", ["stadium_id", "stadium_name", "country_id"], {
            "stadium_id": stadiums.get("id"),
            "stadium_name": stadiums.get("name"),
            "country_id": country.get("id"),
        })

    home_team = match.get("home_team")
    if home_team:
        insert_team(home_team, True)
        if stadiums:
            safe_insert("teams_stadiums", ["team_id", "stadium_id"], {
                "team_id": home_team.get("home_team_id"),
                "stadium_id": stadiums.get("id"),
            })

    away_team = match.get("away_team")
    if away_team:
        insert_team(away_team, False)
        if stadiums:
            safe_insert("teams_stadiums", ["team_id", "stadium_id"], {
                "team_id": away_team.get("away_team_id"),
                "stadium_id": stadiums.get("id"),
            })

    competition = match.get("competition")
    season = match.get("season")
    safe_insert("matches", ["match_id", "match_date", "kick_off", "competition_id", "season_id", "home_score",
                                  "away_score", "match_week", "home_team_id", "away_team_id"], {
                          "match_id": match.get("match_id"),
                          "match_date": match.get("match_date"),
                          "kick_off": match.get("kick_off"),
                          "competition_id": competition.get("competition_id"),
                          "season_id": season.get("season_id"),
                          "home_score": match.get("home_score"),
                          "away_score": match.get("away_score"),
                          "match_week": match.get("match_week"),
                          "home_team_id": home_team.get("home_team_id"),
                          "away_team_id": away_team.get("away_team_id"),
                      })

    if ref:
        safe_insert("matches_referees", ["match_id", "referee_id"], {
            "match_id": match.get("match_id"),
            "referee_id": ref.get("id"),
        })


def insert_team(team, is_home_team):
    country = team.get("country", {})
    safe_insert("countries", ["country_id", "country_name"], {
        "country_id": country.get("id"),
        "country_name": country.get("name")
    })

    if is_home_team:
        team_type = "home_team"
    else:
        team_type = "away_team"

    safe_insert("teams", ["team_id", "team_name", "team_gender", "country_id"], {
        "team_id": team.get(f"{team_type}_id"),
        "team_name": team.get(f"{team_type}_name"),
        "team_gender": team.get(f"{team_type}_gender"),
        "country_id": country.get("id"),
    })

    managers = team.get("managers", [])
    for manager in managers:
        manager_country = manager.get("country", {})
        safe_insert("countries", ["country_id", "country_name"], {
            "country_id": manager_country.get("id"),
            "country_name": manager_country.get("name")
        })

        safe_insert("managers", ["manager_id", "manager_name", "nickname", "dob", "country_id"], {
            "manager_id": manager.get("id"),
            "manager_name": manager.get("name"),
            "nickname": manager.get("nickname"),
            "dob": manager.get("dob"),
            "country_id": manager_country.get("id")
        })

        safe_insert("team_managers", ["team_id", "manager_id"], {
            "team_id": team.get(f"{team_type}_id"),
            "manager_id": manager.get("id"),
        })

def insert_lineups():
    base_url = "data/lineups"

    cursor.execute("SELECT match_id FROM matches;")
    rows = cursor.fetchall()

    match_ids = [id[0] for id in rows]

    for id in match_ids:
        file_path = f"{base_url}/{id}.json"
        with open(file_path, "r", encoding="utf-8") as f:
            lineups = json.load(f)

        for lineup in lineups:
            insert_lineup_for_each_match(lineup, id)


def insert_lineup_for_each_match(lineup, match_id):
    players = lineup.get("lineup", {})
    if players:
        for player in players:
            country = player.get("country", {})

            safe_insert("countries", ["country_id", "country_name"], {
                "country_id": country.get("id"),
                "country_name": country.get("name")
            })

            safe_insert("players", ["player_id", "player_name", "player_nickname", "country_id"], {
                "player_id": player.get("player_id"),
                "player_name": player.get("player_name"),
                "player_nickname": player.get("player_nickname"),
                "country_id": country.get("id")
            })

            lineup_id = safe_insert("lineups", ["match_id", "team_id", "player_id", "jersey_number"], {
                "match_id": match_id,
                "team_id": lineup.get("team_id"),
                "player_id": player.get("player_id"),
                "jersey_number": player.get("jersey_number")
            }, True)

            cards = player.get("cards", [])
            if cards:
                for card in cards:
                    safe_insert("cards", ["time_card", "card_type", "reason", "period", "lineup_id", "player_id", "match_id"], {
                        "time_card": card.get("time"),
                        "card_type": card.get("card_type"),
                        "reason": card.get("reason"),
                        "period": card.get("period"),
                        "lineup_id": lineup_id,
                        "player_id": player.get("player_id"),
                        "match_id": match_id
                    })

            positions = player.get("positions", [])
            if positions:
                for position in positions:
                    safe_insert("positions", ["position_id", "lineup_id", "position", "from_time", "to_time", "from_period",
                                              "to_period", "start_reason", "end_reason", "player_id", "match_id"], {
                        "position_id": position.get("position_id"),
                        "lineup_id": lineup_id,
                        "position": position.get("position"),
                        "from_time": position.get("from"),
                        "to_time": position.get("to"),
                        "from_period": position.get("from_period"),
                        "to_period": position.get("to_period"),
                        "start_reason": position.get("start_reason"),
                        "end_reason": position.get("end_reason"),
                        "player_id": player.get("player_id"),
                        "match_id": match_id
                    })

def insert_events():
    base_url = "data/events"

    cursor.execute("SELECT match_id FROM matches;")
    rows = cursor.fetchall()

    ids = [row[0] for row in rows]

    for id in ids:
        file_path = f"{base_url}/{id}.json"

        with open(file_path, "r", encoding="utf-8") as f:
            events = json.load(f)

        for event in events:
            insert_each_event(event, id)

def insert_each_event(event, match_id):
    event_type = event.get("type")
    team = event.get("team")
    player = event.get("player")

    explicit_columns = {"id", "period", "minute", "second", "type", "team", "location", "related_events"}

    details_dict = {k: v for k, v in event.items() if k not in explicit_columns}

    if event_type:
        safe_insert("event_types", ["event_id", "event_name"], {
            "event_id": event_type.get("id"),
            "event_name": event_type.get("name")
        })

    safe_insert("events", ["event_id", "match_id", "team_id", "player_id", "event_type", "period", "minute",
                           "second", "location", "related_events", "details"], {
        "event_id": event.get("id"),
        "match_id": match_id,
        "team_id": team.get("id") if team else None,
        "player_id": player.get("id") if player else None,
        "event_type": event_type.get("id") if event_type else None,
        "period": event.get("period"),
        "minute": event.get("minute"),
        "second": event.get("second"),
        "location": json.dumps(event.get("location")) if event.get("location") else None,
        "related_events": json.dumps(event.get("related_events")) if event.get("related_events") else None,
        "details": json.dumps(details_dict) if details_dict else None
    })


def safe_insert(table, columns, values_dict, is_lineup_related=False):
    cols_str = ", ".join(columns)
    placeholders = ", ".join(["%s"] * len(columns))

    vals = [values_dict.get(c) for c in columns]

    if all(v is None for v in vals):
        return

    if not is_lineup_related:
        sql_query = f"INSERT INTO {table} ({cols_str}) VALUES ({placeholders}) ON CONFLICT DO NOTHING"
        cursor.execute(sql_query, vals)
        connection.commit()
        return None
    else:
        sql_query = f"INSERT INTO {table} ({cols_str}) VALUES ({placeholders}) ON CONFLICT DO NOTHING RETURNING lineup_id"
        cursor.execute(sql_query, vals)
        lineup_id = cursor.fetchone()[0]
        connection.commit()
        return lineup_id


competitions = insert_competitions()
insert_matches()
insert_lineups()
insert_events()