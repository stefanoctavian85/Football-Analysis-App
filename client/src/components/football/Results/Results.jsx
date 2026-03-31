import { useNavigate, useLocation } from 'react-router-dom';
import './Results.css';
import { useEffect } from 'react';
import { getCurrentTime } from '../../../utils/date.js';
import { useAuth } from '../../../hooks/useAuth.jsx'

function Results() {
    const { accessToken } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const { team, matches } = location.state || [];

    useEffect(() => {
        if (matches.length === 0 || team === null || !accessToken) {
            navigate('/');
        }
    }, [accessToken]);

    async function getDetailsAboutMatch(match) {
        if (match.matchId) {
            navigate(`/match/${match.matchId}`);
        }
    }

    const getResult = (match) => {
        if (match?.winner === team?.teamName) return "W";
        if (match?.winner === "Draw") return "D";
        return "L";
    }

    return (
        <div className='results-page'>
            <div className='results-header'>
                <h1 className='results-title'>{team?.teamName}</h1>
            </div>

            {matches?.length > 0 && (
                <div className="results-table-wrapper">
                    <table className="results-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>MW</th>
                                <th colSpan={3}>Match</th>
                                <th>Result</th>
                            </tr>
                        </thead>
                        <tbody>
                            {matches.map((match, index) => {
                                const result = getResult(match);
                                return (
                                    <tr key={index} onClick={() => getDetailsAboutMatch(match)}>
                                        <td className="results-date">
                                            {new Date(match?.matchDate).toLocaleDateString()} {getCurrentTime(match?.kickOff)}
                                        </td>
                                        <td className="results-mw">{match?.matchWeek}</td>
                                        <td className="results-home">{match?.homeTeamName}</td>
                                        <td className="results-score">{match?.homeScore}:{match?.awayScore}</td>
                                        <td className="results-away">{match?.awayTeamName}</td>
                                        <td className="results-result">
                                            <span className={`result-badge result-${result.toLowerCase()}`}>
                                                {result}
                                            </span>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default Results;