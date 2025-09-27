import { useNavigate, useLocation } from 'react-router-dom';
import './Results.css';
import { useEffect } from 'react';
import { useApi } from '../../../hooks/useApi';
import { getCurrentTime } from '../../../utils/date.js';

function Results() {
    const navigate = useNavigate();
    const location = useLocation();
    const { get } = useApi();
    const { team, matches } = location.state || [];

    useEffect(() => {
        if (matches.length === 0 || team === null) {
            navigate('/');
        }
    }, []);

    async function getDetailsAboutMatch(match) {
        if (match.matchId) {
            navigate(`/match/${match.matchId}`);
        }
    }

    return (
        <div className='matches-page'>
            <div className='matches-header mt-10'>
                <p className='text-center'>Matches played by <span className='font-bold'>{team?.teamName}</span></p>
            </div>

            {
                matches?.length > 0 ? (
                    <div className="table max-w-5xl mx-auto border mt-10 mb-10">
                        <div className='table-row font-bold'>
                            <div className="table-cell text-center p-2">Date</div>
                            <div className="table-cell text-center p-2">MW</div>
                            <div className="table-cell text-center p-2">Match</div>
                            <div className="table-cell text-center p-2">Result</div>
                        </div>

                        {matches.map((match, index) => (
                            <div key={index} className='table-row hover:bg-green-100 cursor-pointer' onClick={() => getDetailsAboutMatch(match)}>
                                <div className="table-cell text-center border-t pl-5 pr-5">
                                    {new Date(match?.matchDate).toLocaleDateString()} {getCurrentTime(match?.kickOff)}
                                </div>

                                <div className='table-cell text-center p-2 border-t pl-5 pr-5'>
                                    {match?.matchWeek}
                                </div>

                                <div className="table-cell border-t px-5 py-2">
                                    <div className='grid grid-cols-3 items-center'>
                                        <span className='whitespace-nowrap text-center'>{match?.homeTeamName}</span>
                                        <span className="font-semibold text-center">{match?.homeScore}:{match?.awayScore}</span>
                                        <span className='whitespace-nowrap text-center'>{match?.awayTeamName}</span>
                                    </div>
                                </div>

                                <div className="table-cell text-center border-t pl-5 pr-5">
                                    {match?.winner === team?.teamName ? "W" : match?.winner === "Draw" ? "D" : "L"}
                                </div>
                            </div>
                        ))}
                    </div>
                ) : null
            }
        </div>
    );
}

export default Results;