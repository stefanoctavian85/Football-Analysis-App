function PitchSVG({ homePlayers, awayPlayers }) {
  function getName(player) {
    let name = player.player?.playerNickname === '' ? player.player?.playerNickname : player.player?.playerName;
    const nameArray = name.split(" ");
    const lastName = nameArray.slice(1).join(" ");
    return lastName;
  }

  return (
    <svg className="min-w-[500px] m-0 p-0 max-w-full" width="700" height="400" viewBox="0 0 700 400">
      <rect x="0" y="0" width="700" height="400" fill="green" />
      <rect x="0" y="0" width="700" height="400" stroke="white" strokeWidth="4" fill="none" />
      <line x1="350" y1="0" x2="350" y2="400" stroke="white" strokeWidth="1" />
      <circle cx="350" cy="200" r="50" fill="none" stroke="white" />

      <rect x="0" y="120" width="80" height="160" fill="none" stroke="white" strokeWidth="1" />
      <path d="M80,160 A40,40 0 0,1 80,240" fill="none" stroke="white" strokeWidth="1" />
      <rect x="0" y="160" width="10" height="80" fill="none" stroke="white" strokeWidth="1" />

      {
        homePlayers.map((player, index) => (
          <g key={index}>
            <circle cx={player.x} cy={player.y} r={15} fill="blue" stroke="white" strokeWidth={1} />
            <text
              x={player.x}
              y={player.y}
              textAnchor="middle"
              alignmentBaseline="middle"
              fill="white"
              fontSize="12"
              fontWeight="bold"
            >
              {player.jerseyNumber}
            </text>

            <text
              x={player.x}
              y={player.y + 25}
              textAnchor="middle"
              alignmentBaseline="hanging"
              fill="black"
              fontSize="10"
              fontWeight="bold"
            >
              {getName(player)}
            </text>
          </g>
        ))
      }

      {
        awayPlayers.map((player, index) => (
          <g key={index}>
            <circle cx={player.x} cy={player.y} r={15} fill="red" stroke="white" strokeWidth={1} />
            <text
              x={player.x}
              y={player.y}
              textAnchor="middle"
              alignmentBaseline="middle"
              fill="white"
              fontSize="12"
              fontWeight="bold"
            >
              {player.jerseyNumber}
            </text>

            <text
              x={player.x}
              y={player.y + 25}
              textAnchor="middle"
              alignmentBaseline="hanging"
              fill="black"
              fontSize="10"
              fontWeight="bold"
            >
              {getName(player)}
            </text>
          </g>
        ))
      }

      <rect x="620" y="120" width="80" height="160" fill="none" stroke="white" strokeWidth="1" />
      <path d="M620,160 A40,40 0 0,0 620,240" fill="none" stroke="white" strokeWidth="1" />
      <rect x="690" y="160" width="10" height="80" fill="none" stroke="white" strokeWidth="1" />
    </svg>
  );
}

export default PitchSVG;