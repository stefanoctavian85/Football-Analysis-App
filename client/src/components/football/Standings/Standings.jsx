import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Standings.css';
import { useAuth } from '../../../hooks/useAuth';
import { useApi } from '../../../hooks/useApi';
import { ENDPOINTS } from '../../../config/constants';
import { PAGE_LIMIT } from '../../../utils/constants';
import Pagination from '../../pagination/Pagination';

function Standings() {
    const { accessToken } = useAuth();
    const { get } = useApi();
    const [groupedCompetitions, setGroupedCompetitions] = useState({});
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const navigate = useNavigate();

    useEffect(() => {
        if (accessToken) {
            getCompetitions()
        } else {
            navigate("/");
        }
    }, [accessToken, page]);

    async function getCompetitions() {
        try {
            const data = await get(`${ENDPOINTS.COMPETITIONS}?page=${page}&pageLimit=${PAGE_LIMIT}`);

            const competitions = data.competitions.reduce((accumulator, competition) => {
                const key = competition.countryName;
                if (!accumulator[key]) accumulator[key] = [];
                accumulator[key].push(competition);
                return accumulator;
            }, {});

            setGroupedCompetitions(competitions);
            setTotalPages(data.totalPages);
        } catch (error) {
            console.error(error.message);
        }
    }

    return (
        <div className="standings-page">
            <div className="standings-header">
                <h1 className="standings-title">Competitions</h1>
            </div>
            <div className="competitions-list">
                {Object.entries(groupedCompetitions).map(([country, comps]) => (
                    <div className="country-group" key={country}>
                        <div className="country-header">
                            <span className="country-name">{country}</span>
                        </div>
                        {comps.map((value, index) => (
                            <Link
                                key={index}
                                to={`/standings/${encodeURIComponent(value.competitionName)}`}
                                state={{ championship: value }}
                                className="competition-row"
                            >
                                <span className="competition-row-name">{value.competitionName}</span>
                                <span className={`gender-badge gender-${value.competitionGender?.toLowerCase()}`}>
                                    {value.competitionGender}
                                </span>
                                <span className="competition-row-arrow">›</span>
                            </Link>
                        ))}
                    </div>
                ))}
            </div>
            <Pagination page={page} onPageChange={setPage} totalPages={totalPages} />
        </div>
    );
}

export default Standings;