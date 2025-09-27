export function getCurrentTime(date) {
    let array = date.split(":");
    return array[0] + ":" + array[1];
}

export function getDate(date) {
    let array = date.split("T");
    let matchDate = array[0].split("-");
    return matchDate[2] + "." + matchDate[1] + "." + matchDate[0];
}