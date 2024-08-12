const naverMapURL = 'http://map.naver.com/index.nhn?';

const loadingModal = document.querySelector(".loading-modal");
let addressModal = new bootstrap.Modal(document.getElementById('address-modal'), {
    keyboard: false
});
let element_wrap = document.getElementById('wrap');
let themeObj = {
    searchBgColor: "#0B65C8",
    queryTextColor: "#FFFFFF"
};
let kakaoMap;

function getAddress() {
    let currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        theme : themeObj,
        oncomplete: function(data) {
            document.body.scrollTop = currentScroll;
            additionPlaceAddressToCoordinate(data.roadAddress);
            addressModal.hide();
            loadingModal.style.display = "block";
        },
        width : '100%',
        height : '100%'
    }).embed(element_wrap,{ autoClose: false });
    element_wrap.style.display = 'block';
}
document.querySelector("#address-button").addEventListener("click",function(evt){
    getAddress();
    addressModal.show();
});
const currentPlaceHandler = async() => {
    loadingModal.style.display = "block";
    await getForCurrentLocation(getCurrentPlaceList);
}
const getCurrentPlaceList = async (position) => {
    const coordinateRequest = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
    }
    let data;
    if (type === 'cafe'){
        data = await getAroundPetCafeProcess(coordinateRequest);
    }else{
        data =  await getAroundPetEducationProcess(coordinateRequest);
    }
    await additionResultContainerHandler(data,coordinateRequest);
};
const additionPlaceAddressToCoordinate = async (address) => {
    const coordinateResponseData = await addressToCoordinateProcess(address);
    if (coordinateResponseData){
        await getAroundPlaceList(coordinateResponseData);
    }
};
const getAroundPlaceList = async (coordinateRequest) => {
    let data;
    if (type === 'cafe'){
        data = await getAroundPetCafeProcess(coordinateRequest);
    }else{
        data =  await getAroundPetEducationProcess(coordinateRequest);
    }
    await additionResultContainerHandler(data,coordinateRequest);
};

const additionResultContainerHandler = async (data,coordinateRequest) => {

    const trafficModals = document.querySelectorAll('[id^="traffic-modal"]');
    trafficModals.forEach(item => {
        item.remove();
    })

    let innerText = "";
    const resultContainer = document.querySelector(".result-container");

    let mapContainerBox = document.getElementById('map-wrapper');
    let mapContainer = document.getElementById("map");

    mapContainerBox.style.display="block";
    let options = {
        center: new kakao.maps.LatLng(coordinateRequest.latitude, coordinateRequest.longitude),
        level: 7
    };
    let map = new kakao.maps.Map(mapContainer, options);
    kakaoMap = map;
    let centerMarkerPosition  = new kakao.maps.LatLng(coordinateRequest.latitude, coordinateRequest.longitude);
    let marker = new kakao.maps.Marker({
        position: centerMarkerPosition
    });
    marker.setMap(map);
    let imageSrc;
    let category;
    if (type === 'cafe'){
        imageSrc = "../../assets/cafe.png";
        category = '애견 카페';
    }else{
        imageSrc = "../../assets/school.png";
        category = '애견 훈련소';
    }
    innerText += `
        <div class="wrapper animation-tag">
            <h3 class="result-title"># ${category} 검색 결과</h3>
            <p class="result-description">
                ${category}에 대한 검색 결과입니다. <br> 
                근처 ${category}는 ${data.length}개 존재합니다. 지도와 함께 교통정보도 확인해보세요! <br>
            </p>
            <div class="result-list-wrapper">
                    <div class="first-list"></div>
                    <div class="second-list"></div>
                    <div class="third-list"></div>
                </div>
        </div>
    `
    resultContainer.innerHTML = innerText;

    const modalContainerList = document.querySelector(".modal-container");
    if (data.length === 0){
        document.querySelector(".result-description").innerHTML = '해당 지역 근처에 애견 카페가 존재하지 않습니다';
        document.querySelector(".first-list").style.display="none";
        document.querySelector(".second-list").style.display="none";
        document.querySelector(".third-list").style.display="none";
    }else if (data.length <= 5){
        document.querySelector(".second-list").style.display="none";
        document.querySelector(".third-list").style.display="none";
    }else if (data.length <= 10){
        document.querySelector(".third-list").style.display="none";
    }
    for(let i=0;i<data.length;i+=5){
        if (i > data.length - 1){
            loadingModal.style.display = "none";
            return;
        }
        for(let j=i;j<i+5;j++){
            if (j > data.length - 1){
                loadingModal.style.display = "none";
                return;
            }
            let targetDiv;
            if (i === 0){
                targetDiv = document.querySelector(".first-list");
            }else if (i === 5){
                targetDiv = document.querySelector(".second-list");
            }else{
                targetDiv = document.querySelector(".third-list");
            }
            let imageSize = new kakao.maps.Size(45, 45);
            let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

            let placeMarker = new kakao.maps.Marker({
                map: map,
                position:  new kakao.maps.LatLng(data[j].latitude, data[j].longitude),
                image : markerImage
            });
            let content =`
                <div class="customOverlay">
                    <p class="customOverlay-placeName">${data[j].placeName}</p>
                    <p class="customOverlay-distance">${data[j].routeResponse.distance}</p>
                </div>
            `
            let customOverlay = new kakao.maps.CustomOverlay({
                position:  new kakao.maps.LatLng(data[j].latitude, data[j].longitude),
                content: content,
                yAnchor: 1,
            });
            kakao.maps.event.addListener(placeMarker, 'mouseover', makeOverListener(map, placeMarker, customOverlay));
            kakao.maps.event.addListener(placeMarker, 'mouseout', makeOutListener(customOverlay));
            kakao.maps.event.addListener(customOverlay, 'mouseover', makeOverListener(map, placeMarker, customOverlay));
            kakao.maps.event.addListener(customOverlay, 'mouseout', makeOutListener(customOverlay));


            const slng = coordinateRequest.longitude;
            const slat = coordinateRequest.latitude;

            const elng = data[j].longitude;
            const elat = data[j].latitude;
            const etext = data[j].placeName;

            let cardContainer = document.createElement("div");
            let modalContainer = document.createElement("div");

            cardContainer.classList.add('place-card-wrapper');
            let phoneText;
            if (data[j].phoneNumber.trim().length === 0){
                phoneText = '아직 등록된 연락처가 없습니다.'
            }else{
                phoneText = data[j].phoneNumber;
            }
            cardContainer.innerHTML = `
                <h5 class="place-title"># ${data[j].placeName}</h5>
                <div class="front">
                    <ul class="place-detail-wrapper">
                       <li class="place-detail">
                            <p> <i class="fa-solid fa-icons"></i> 카테고리</p>
                            <span>${data[j].category}</span>
                        </li>
                         <li class="place-detail">
                            <p><i class="fa-solid fa-route"></i> 총 거리</p>
                            <span>${data[j].routeResponse.distance}</span>
                        </li>
                        <li class="place-detail">
                            <p><i class="fa-solid fa-location-dot"></i> 주소</p>
                            <span>${data[j].address}</span>
                        </li>
                        <li class="place-detail">
                            <p><i class="fa-solid fa-phone-volume"></i> 연락처</p>
                            <span>${phoneText}</span>
                        </li>
                    </ul>
                    <div class="button-container">
                        <button class="traffic-button" 
                            data-bs-toggle="modal" 
                            data-bs-target="#traffic-modal${j}">
                                <i class="fa-solid fa-car"></i> 교통정보
                        </button>
                        <a class="place-button" 
                            target="_blank"
                            href="${data[j].placeURL}">
                            <i class="fa-solid fa-location-arrow"></i> 카카오맵
                        </a>
                    </div>
                </div>
            `;
            modalContainer.innerHTML = `
                 <div class="modal fade" id="traffic-modal${j}" tabindex="-1" aria-labelledby="traffic-modal" aria-hidden="true">
                    <div class="modal-dialog  modal-dialog-centered traffic-modal">
                        <div class="modal-content">
                            <div class="modal-header traffic-header">
                                <h1 class="modal-title fs-5"># ${data[j].placeName}</h1>
                            </div>
                            <div class="modal-body">
                                <div class="back">
                                    <ul class="place-detail-wrapper">
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 총 거리 </p>
                                            <span>${data[j].routeResponse.distance}</span>
                                        </li>
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 차량 소요 시간</p>
                                            <span>${data[j].routeResponse.time}</span>
                                        </li>
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 택시 예상 요금</p>
                                            <span>${data[j].routeResponse.taxiFare}</span>
                                        </li>
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 톨게이트 예상 요금</p>
                                            <span>${data[j].routeResponse.tollFare}</span>
                                        </li>
                                    </ul>
                                    <div class="route-button-container">
                                        <a 
                                            target="_blank"
                                            href="${naverMapURL}slng=${slng}&slat=${slat}&stext=${'검색 위치'}&elng=${elng}&elat=${elat}&pathType=0&showMap=true&etext=${etext}&menu=route" class="route-button">
                                            <i class="fa-solid fa-route"></i> 길찾기 바로가기
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `
            targetDiv.appendChild(cardContainer);
            modalContainerList.appendChild(modalContainer);
        }
    }
    loadingModal.style.display = "none";
}
