CREATE TABLE COMPETITIONS (
        competition_id INT,
        season_id INT,
        country_name TEXT,
        competition_name TEXT,
        competition_gender TEXT,
        season_name TEXT,
        PRIMARY KEY (competition_id, season_id)
);

CREATE TABLE matches (  
        match_id INT PRIMARY KEY,
        match_date DATE,
        kick_off TIME,
        competition_id INT,
        season_id INT,
        home_score INT,
        away_score INT,
        match_week INT,
        FOREIGN KEY (competition_id, season_id) REFERENCES competitions(competition_id, season_id)
);

CREATE TABLE countries (
	    country_id INT PRIMARY KEY,
	    country_name TEXT
);

CREATE TABLE teams (
    	team_id INT PRIMARY KEY,
    	team_name TEXT,
    	team_gender TEXT,
    	country_id INT,
    	FOREIGN KEY (country_id) REFERENCES countries(country_id)
);

CREATE TABLE managers (
	    manager_id INT PRIMARY KEY,
    	name TEXT,
    	nickname TEXT,
    	dob DATE,
    	country_id INT,
    	FOREIGN KEY (country_id) REFERENCES countries(country_id)
);

CREATE TABLE team_managers (
	    team_id INT,
    	manager_id INT,
    	PRIMARY KEY(team_id, manager_id),
    	FOREIGN KEY (team_id) REFERENCES teams(team_id),
    	FOREIGN KEY (manager_id) REFERENCES managers(manager_id)
);

CREATE TABLE stadiums(
	    stadium_id INT PRIMARY KEY,
    	stadium_name TEXT,
    	country_id INT,
    	FOREIGN KEY (country_id) REFERENCES countries(country_id)
);

CREATE TABLE teams_stadiums (
	    team_id INT,
    	stadium_id INT,
    	PRIMARY KEY(team_id, stadium_id),
    	FOREIGN KEY (team_id) REFERENCES teams(team_id),
    	FOREIGN KEY (stadium_id) REFERENCES stadiums(stadium_id)
);

CREATE TABLE referees(
	    referee_id INT PRIMARY KEY,
    	referee_name TEXT,
    	country_id INT,
    	FOREIGN KEY (country_id) REFERENCES countries(country_id)
);

CREATE TABLE matches_referees (
	    match_id INT,
    	referee_id INT,
    	PRIMARY KEY (match_id, referee_id),
    	FOREIGN KEY (match_id) REFERENCES matches(match_id),
    	FOREIGN KEY (referee_id) REFERENCES referees(referee_id)
);

ALTER TABLE managers
RENAME COLUMN name TO manager_name;

ALTER TABLE matches
ADD COLUMN home_team_id INT,
ADD COLUMN away_team_id INT,
ADD FOREIGN KEY (home_team_id) REFERENCES teams(team_id),
ADD FOREIGN KEY (away_team_id) REFERENCES teams(team_id);

CREATE TABLE players (
	    player_id INT PRIMARY KEY,
    	player_name TEXT,
    	player_nickname TEXT,
    	country_id INT,
    	FOREIGN KEY (country_id) REFERENCES countries(country_id)
);

CREATE TABLE lineups (
	    lineup_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    	match_id INT,
    	team_id INT,
    	player_id INT,
    	jersey_number INT,
    	FOREIGN KEY (match_id) REFERENCES matches(match_id),
    	FOREIGN KEY (team_id) REFERENCES teams(team_id),
    	FOREIGN KEY (player_id) REFERENCES players(player_id)
);

CREATE TABLE cards (
        card_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        time_card TEXT,
        card_type TEXT,
        reason TEXT,
        period INT,
    	lineup_id INT,
    	FOREIGN KEY (lineup_id) REFERENCES lineups(lineup_id)
);

CREATE TABLE positions (
    	position_id INT,
    	lineup_id INT,
    	position TEXT,
    	from_time TEXT,
    	to_time TEXT,
    	from_period INT,
    	to_period INT,
    	start_reason TEXT,
    	end_reason TEXT,
    	PRIMARY KEY (position_id, lineup_id),
    	FOREIGN KEY (lineup_id) REFERENCES lineups(lineup_id)
);

CREATE TABLE events (
        event_id UUID PRIMARY KEY,
        match_id INT,
        team_id INT,
        player_id INT,
        event_type INT,
        period INT,
        minute INT,
        second INT,
        location JSONB,
        related_events JSONB,
        details JSONB,
        FOREIGN KEY (match_id) REFERENCES matches(match_id)
);

CREATE TABLE event_types (
        event_id INT PRIMARY KEY,
        event_name TEXT
);

ALTER TABLE events
ADD FOREIGN KEY (event_type) REFERENCES event_types(event_id);
