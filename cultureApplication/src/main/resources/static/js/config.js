axios.defaults.paramsSerializer = function (paramObj) {
    const params = new URLSearchParams();
    for (const key in paramObj) {
        params.append(key, paramObj[key]);
    }
    return params.toString();
};

// GeoLocation
async function getForCurrentLocation (callbackFunction) {
    await navigator.geolocation.getCurrentPosition(callbackFunction)
}

