import { useEffect, useState } from 'react';
import './Championship.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { useApi } from '../../../hooks/useApi';
import { useAuth } from '../../../hooks/useAuth';
import { ENDPOINTS } from '../../../config/constants';

function Championship() {
    const { accessToken } = useAuth();
    const { get, context } = useApi();
    const location = useLocation();
    const { championship } = location.state || {};
    const navigate = useNavigate();
    const [selectedSeason, setSelectedSeason] = useState('');

    const [standings, setStandings] = useState([]);

    useEffect(() => {
        if (accessToken) {
            if (championship === null) {
                navigate('/');
            } else {
                if (context.getSeason() !== '') {
                    setSelectedSeason(context.getSeason());
                    getSeason();
                }
            }
        } else {
            navigate("/");
        }
    }, [accessToken]);

    async function getSeason() {
        const seasonToUse = selectedSeason != '' ? selectedSeason : context.getSeason();

        if (seasonToUse != '') {
            try {
                const data = await get(`${ENDPOINTS.COMPETITIONS}/standings/${championship.competitionId}/${seasonToUse}`);
                setStandings(data.standings);
            } catch (error) {
                console.error(error.message);
            }
        }
    }

    async function getMatchesForSpecificTeam(team) {
        if (team.teamId != 0 || team.teamId != null) {
            try {
                const data = await get(`${ENDPOINTS.MATCHES}/${championship.competitionId}/${selectedSeason}/${team.teamId}`);
                if (data.matches.length > 0) {
                    navigate(`/standings/${encodeURIComponent(championship.competitionName)}/${team.teamName}`, {
                        state: {
                            matches: data.matches,
                            team: team,
                        }
                    });
                }
            } catch (error) {
                console.error(error.message);
            }
        }
    }

    return (
        <div className='championship-page'>
            <div className='championship-header'>
                <h1 className='championship-title'>{championship.competitionName}</h1>
            </div>

            <div className='championship-season-form'>
                <label className='championship-season-label'>Season</label>
                <select
                    className='championship-season-select'
                    value={selectedSeason}
                    onChange={(e) => {
                        context.setSeason(e.target.value);
                        setSelectedSeason(e.target.value);
                    }}
                >
                    <option value="">Choose a season</option>
                    {
                        championship.seasons.map((season, index) => (
                            <option value={season.seasonId} key={index}>
                                {season.seasonName}
                            </option>
                        ))
                    }
                </select>
                <button
                    className="championship-season-btn"
                    type="button"
                    onClick={getSeason}
                    disabled={selectedSeason === ''}
                >
                    Show
                </button>
            </div>

            {standings.length > 0 && (
                <div className="championship-standings">
                    <table className="standings-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Team</th>
                                <th>MP</th>
                                <th>W</th>
                                <th>D</th>
                                <th>L</th>
                                <th>Goals</th>
                                <th>GD</th>
                                <th>Pts</th>
                            </tr>
                        </thead>
                        <tbody>
                            {standings.map((value, index) => (
                                <tr key={index}>
                                    <td className="standings-rank">{index + 1}</td>
                                    <td onClick={() => getMatchesForSpecificTeam(value)}>{value?.teamName}</td>
                                    <td>{value?.nrMatchesPlayed}</td>
                                    <td>{value?.nrWins}</td>
                                    <td>{value?.nrDraws}</td>
                                    <td>{value?.nrLosses}</td>
                                    <td>{value?.goalsScored}:{value?.goalsConceded}</td>
                                    <td>{value?.goalsDifference}</td>
                                    <td className="standings-points">{value?.nrPoints}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default Championship;