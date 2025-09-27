import { useEffect, useState } from 'react';
import React from 'react';
import './Championship.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { useApi } from '../../../hooks/useApi';
import { ENDPOINTS } from '../../../config/constants';

function Championship() {
    const { get, context } = useApi();
    const location = useLocation();
    const { championship } = location.state || {};
    const navigate = useNavigate();
    const [selectedSeason, setSelectedSeason] = useState('');

    const [standings, setStandings] = useState([]);

    useEffect(() => {
        if (championship === null) {
            navigate('/');
        } else {
            if (context.getSeason() !== '') {
                setSelectedSeason(context.getSeason());
                getSeason();
            }
        }
    }, []);

    async function getSeason() {
        if (selectedSeason !== '') {
            try {
                const data = await get(`${ENDPOINTS.COMPETITIONS}/standings/${championship.competitionId}/${selectedSeason}`);
                setStandings(data.standings);
            } catch (error) {
                console.error(error.message);
            }
        } else {
            if (context.getSeason() !== '') {
                try {
                    const data = await get(`${ENDPOINTS.COMPETITIONS}/standings/${championship.competitionId}/${context.getSeason()}`);
                    setStandings(data.standings);
                } catch (error) {
                    console.error(error.message);
                }
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
            <div className='championship-header mt-10'>
                <p className='text-center font-semibold text-lg'>{championship.competitionName}</p>
            </div>

            <div className='championship-content mt-5'>
                <form>
                    <label>Select a season:</label>
                    <select
                        value={selectedSeason}
                        onChange={(e) => {
                            context.setSeason(e.target.value);
                            setSelectedSeason(e.target.value)
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
                        type='button'
                        onClick={getSeason}
                    >Show season</button>
                </form>
            </div>

            {
                standings.length > 0 ? (
                    <div className='grid grid-cols-9 w-full border text-sm mt-5'>
                        <div className="font-bold bg-gray-100 p-2 text-center">#</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">Team</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">MP</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">W</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">D</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">L</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">Goals</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">GD</div>
                        <div className="font-bold bg-gray-100 p-2 text-center">Points</div>

                        {standings.map((value, index) => (
                            <React.Fragment key={index}>
                                <div className="p-2 text-center border-t">
                                    {index + 1}
                                </div>

                                <div className="p-2 text-center border-t" onClick={() => getMatchesForSpecificTeam(value)}>
                                    {value?.teamName}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.nrMatchesPlayed}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.nrWins}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.nrDraws}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.nrLosses}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.goalsScored} : {value?.goalsConceded}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.goalsDifference}
                                </div>

                                <div className="p-2 text-center border-t">
                                    {value?.nrPoints}
                                </div>
                            </React.Fragment>
                        ))}
                    </div>
                ) : null
            }

        </div>
    );
}

export default Championship;