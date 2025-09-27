class ChampionshipStore {
    constructor() {
        this.season = '';
    }

    setSeason(season) {
        this.season = season;
    }

    getSeason() {
        return this.season;
    }
}

export default ChampionshipStore;