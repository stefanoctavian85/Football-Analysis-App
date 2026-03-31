import './Home.css';

function Home() {
    return (
        <div className="home-page">
            <div className="home-hero">
                <span className="home-eyebrow">Football Analytics</span>
                <h1 className="home-title">Footprint</h1>
                <p className="home-subtitle">
                    Standings, results and detailed match data for competitions around the world.
                </p>
            </div>

            <div className="home-divider" />

            <div className="home-stats">
                <div className="home-stat">
                    <span className="home-stat-number">50+</span>
                    <span className="home-stat-label">Competitions</span>
                </div>
                <div className="home-stat">
                    <span className="home-stat-number">900+</span>
                    <span className="home-stat-label">Teams</span>
                </div>
                <div className="home-stat">
                    <span className="home-stat-number">15K+</span>
                    <span className="home-stat-label">Matches</span>
                </div>
            </div>
        </div>
    );
}

export default Home;