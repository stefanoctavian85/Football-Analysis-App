import './Lineup.css';
import { useEffect, useState } from "react";

function Lineup({ players }) {
    const [firstEleven, setFirstEleven] = useState([]);

    useEffect(() => {
        setFirstEleven(sortPlayersByJerseyNumber(players));
    }, []);

    function sortPlayersByJerseyNumber(players) {
        let firstEleven = players.filter((element, index) => {
            if (element?.positions[0]?.fromTime === "00:00") {
                return element;
            }
        })

        firstEleven.sort((a, b) => {
            if (a.positions[0].position === "Goalkeeper" && b.positions[0].position !== "Goalkeeper") {
                return -1;
            } else if (a.positions[0].position !== "Goalkeeper" && b.positions[0].position === "Goalkeeper") {
                return 1;
            } else {
                return a.jerseyNumber - b.jerseyNumber;
            }
        })
        console.log(players.length);
        console.log(firstEleven.length);
        return firstEleven;
    }

    return (
        <div className="lineup-component">
            <div className="lineup-players text-left">
                <ul>
                    {
                        firstEleven.map((element, index) => {
                            return (
                                <li key={index}>
                                    <div className="lineup-player">
                                        {element?.jerseyNumber}. {element?.player.playerNickname || element?.player.playerName}
                                    </div>
                                </li>)
                        }
                        )}
                </ul>
            </div>
        </div>
    );
}

export default Lineup;