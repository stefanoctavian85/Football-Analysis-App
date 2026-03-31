import { useEffect, useState } from 'react';
import './Match.css';
import { useParams, useNavigate } from 'react-router-dom';
import { useApi } from '../../../hooks/useApi';
import { useAuth } from '../../../hooks/useAuth';
import { ENDPOINTS } from '../../../config/constants';
import PitchSVG from '../Pitch/Pitch';
import { getCurrentTime, getDate } from '../../../utils/date';
import Lineup from '../Lineup/Lineup';

function Match() {
    const { matchId } = useParams();
    const { get } = useApi();
    const { accessToken } = useAuth();
    const [matchDetails, setMatchDetails] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        if (accessToken) {
            getMatchDetails();
        } else {
            navigate('/');
        }
    }, [accessToken]);

    async function getMatchDetails() {
        try {
            const data = await get(`${ENDPOINTS.MATCHES}/match/${matchId}`);
            setMatchDetails(data.lineups);
            setIsLoading(false);
            getStartingPlayers(data.lineups.homeTeam.players);
        } catch (error) {
            console.error(error.message);
        }
    }

    function getStartingPlayers(teamPlayers, isHomeTeam) {
        const startingEleven = teamPlayers.filter(player => player.positions[0]?.startReason === "Starting XI");
        const lines = {
            goalkeeper: [],
            defenders: [],
            midfielders: [],
            attackers: [],
        };

        startingEleven.forEach((player) => {
            const position = player.positions[0]?.position;
            if (position === "Goalkeeper") {
                lines.goalkeeper.push(player);
            } else if (["Left Back", "Left Center Back", "Center Back", "Right Center Back", "Right Back",
                "Left Wing Back", "Right Wing Back"].includes(position)) {
                lines.defenders.push(player);
            } else if (["Left Defensive Midfield", "Center Defensive Midfield", "Right Defensive Midfield",
                "Left Center Midfield", "Center Midfield", "Right Center Midfield",
                "Left Midfield", "Right Midfield",
                "Left Attacking Midfield", "Center Attacking Midfield", "Right Attacking Midfield"].includes(position)) {
                lines.midfielders.push(player);
            } else {
                lines.attackers.push(player);
            }
        });

        lines.defenders = sortBySide(lines.defenders);
        lines.midfielders = sortBySide(lines.midfielders);

        const totalHeight = 400;
        let xMap = {};
        if (isHomeTeam === true) {
            xMap = {
                goalkeeper: 30,
                defenders: 120,
                midfielders: 220,
                attackers: 320,
            }
        } else {
            xMap = {
                goalkeeper: 670,
                defenders: 580,
                midfielders: 480,
                attackers: 380,
            }
        }

        const svgPlayers = [];

        Object.keys(lines).forEach(line => {
            const players = lines[line];
            const spacing = totalHeight / (players.length + 1);
            const x = xMap[line];

            players.forEach((player, index) => {
                const y = spacing * (index + 1);
                svgPlayers.push({ ...player, x, y });
            })
        })

        return svgPlayers;
    }

    function sortBySide(players) {
        const order = {
            Left: -1,
            Center: 0,
            Right: 1,
        }

        return players.sort((a, b) => {
            const posA = a.positions[0]?.position;
            const posB = b.positions[0]?.position;

            const sideA = Object.keys(order).find(key => posA.includes(key)) || "Center";
            const sideB = Object.keys(order).find(key => posB.includes(key)) || "Center";

            return order[sideA] - order[sideB];
        })
    }

    if (isLoading) {
        return (
            <div className="match-page">
                <p style={{ fontSize: '20px', marginTop: '48px', display: 'flex', justifyContent: 'center' }}>Loading...</p>
            </div>
        );
    }

    return (
        <div className="match-page">
            <div className="match-header">
                <span className="match-header-team home">{matchDetails?.match.homeTeamName}</span>

                <div className="match-header-center">
                    <span className="match-header-score">
                        {matchDetails?.match.homeScore} - {matchDetails?.match.awayScore}
                    </span>
                    <span className="match-header-date">{getDate(matchDetails?.match.matchDate)}</span>
                    <span className="match-header-hour">{getCurrentTime(matchDetails?.match.kickOff)}</span>
                </div>

                <span className="match-header-team away">{matchDetails?.match.awayTeamName}</span>
            </div>

            <div className="match-content">
                <Lineup players={matchDetails?.homeTeam.players} />
                <div className="match-content-center">
                    <PitchSVG
                        homePlayers={getStartingPlayers(matchDetails?.homeTeam.players, true)}
                        awayPlayers={getStartingPlayers(matchDetails?.awayTeam.players, false)}
                    />
                </div>
                <Lineup players={matchDetails?.awayTeam.players} />
            </div>
        </div>
    );
}

export default Match;