import { useEffect, useState } from 'react';
import './Match.css';
import { useParams } from 'react-router-dom';
import { useApi } from '../../../hooks/useApi';
import { ENDPOINTS } from '../../../config/constants';
import PitchSVG from '../Pitch/Pitch';
import { getCurrentTime, getDate } from '../../../utils/date';

function Match() {
    const { matchId } = useParams();
    const { get } = useApi();
    const [matchDetails, setMatchDetails] = useState();
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        getMatchDetails();
    }, []);

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

        startingEleven.forEach((player, _) => {
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
            <div>Waiting...</div>
        );
    }

    return (
        <div className='match-page max-w-full'>
            <div className='match-header mt-10 flex flex-row justify-center gap-[60px]'>
                <div className='match-header-left'>
                    <p>{matchDetails?.match.homeTeamName}</p>
                </div>

                <div className='match-header-center flex flex-col items-center justify-center'>
                    <div className='match-header-score'>
                        <p className='text-right'>{matchDetails?.match.homeScore} - {matchDetails?.match.awayScore}</p>
                    </div>
                    <div className='match-header-date'>
                        <p className='text-center'>{getDate(matchDetails?.match.matchDate)}</p>
                    </div>
                    <div className='match-header-hour'>
                        <p className='text-left'>{getCurrentTime(matchDetails?.match.kickOff)}</p>
                    </div>
                </div>

                <div className='match-header-right'>
                    <p>{matchDetails?.match.awayTeamName}</p>
                </div>
            </div>

            <div className='match-content mt-16 flex justify-center'>
                <PitchSVG homePlayers={getStartingPlayers(matchDetails?.homeTeam.players, true)} awayPlayers={getStartingPlayers(matchDetails?.awayTeam.players, false)} />
            </div>
        </div>
    );
}

export default Match;