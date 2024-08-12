let singlePlaceModal;

function makeOverListener(map, marker, customOverlay) {
    return function() {
        customOverlay.setMap(map);
    };
}
function makeOutListener(customOverlay) {
    return function() {
        customOverlay.setMap(null);
    };
}
function zoomIn() {
    kakaoMap.setLevel(kakaoMap.getLevel() - 1);
}
function zoomOut() {
    kakaoMap.setLevel(kakaoMap.getLevel() + 1);
}
const kakaoPlaceModalOpenHandler = (placeId) => {
    return function() {
        singlePlaceModal = new bootstrap.Modal(document.getElementById(`place-modal${placeId}`), {
            keyboard: false
        });
        singlePlaceModal.show();
    };
};