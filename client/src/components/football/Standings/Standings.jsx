import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import './Standings.css';
import { useAuth } from '../../../hooks/useAuth';
import { useApi } from '../../../hooks/useApi';
import { ENDPOINTS } from '../../../config/constants';

function Standings() {
    const { accessToken } = useAuth();
    const { get } = useApi();
    const [competitions, setCompetitions] = useState([]);

    useEffect(() => {
        if (accessToken) {
            getCompetitions();
        }
    }, [accessToken]);

    async function getCompetitions() {
        try {
            const data = await get(`${ENDPOINTS.COMPETITIONS}/all-competitions`);
            setCompetitions(data.competitions);
        } catch (error) {
            console.error(error.message);
        }
    }

    return (
        <div className="standings-page">
            <div className='competitions-list'>
                {
                    competitions.map((value, index) => (
                        <div className='competition-card' key={index}>
                            <div className='competition-card-upper-side'>
                                <div className='competition-card-left-side'>
                                    <p>{value.competitionName}</p>
                                    <p>{value.countryName}</p>
                                </div>
                                <div className='competition-card-right-side'>
                                    <p>{value.competitionGender}</p>
                                </div>
                            </div>
                            <div className='competition-card-lower-side'>
                                <Link
                                    to={`/standings/${encodeURIComponent(value.competitionName)}`}
                                    state={{ championship: value }}
                                >
                                    Select
                                </Link>
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    )
}

export default Standings;