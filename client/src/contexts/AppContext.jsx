import { createContext } from "react";
import ChampionshipStore from "../stores/ChampionshipStore.jsx";

export const AppContext = createContext();

const championshipStore = new ChampionshipStore();

export function AppProvider({children}) {
    return (
        <AppContext.Provider value={championshipStore}>
            {children}
        </AppContext.Provider>
    )
}