const naverMapURL = 'http://map.naver.com/index.nhn?';

const loadingModal = document.querySelector(".loading-modal");
let addressModal = new bootstrap.Modal(document.getElementById('address-modal'), {
    keyboard: false
});
let searchModal = new bootstrap.Modal(document.getElementById('search-modal'), {
    keyboard: false
});
let element_wrap = document.getElementById('wrap');
let themeObj = {
    searchBgColor: "#0B65C8",
    queryTextColor: "#FFFFFF"
};
let kakaoMap;
// 장소 검색
const searchModalHandler = () => {
    searchModal.show();
}
// 현재 위치 검색
const currentPlaceHandler = async () => {
    loadingModal.style.display="block";
    await getForCurrentLocation(getCurrentPlaceList);
};
// 주소 검색
document.querySelector("#address-button").addEventListener("click",async function(evt){
    await getAddress();
    addressModal.show();
});
async function getAddress() {
    let currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        theme : themeObj,
        oncomplete: async function(data) {
            document.body.scrollTop = currentScroll;
            addressToCoordinate(data.roadAddress);
            addressModal.hide();
            loadingModal.style.display = "block";
        },
        width : '100%',
        height : '100%'
    }).embed(element_wrap,{ autoClose: false });
    element_wrap.style.display = 'block';
}
const getPlaceMarkerImageSrc = (category) => {
    let imageSrc;
    switch (category){
        case '반려동물용품':
        case '미용':
        case '위탁관리':
            imageSrc = "../../assets/shop.png";
            break;
        case '여행지':
            imageSrc = "../../assets/tour.png";
            break;
        case '박물관':
        case '미술관':
            imageSrc = "../../assets/museum.png";
            break;
        case '문예회관':
            imageSrc = "../../assets/culture.png";
            break;
        case '카페':
            imageSrc = "../../assets/cafe.png";
            break;
        case '식당':
            imageSrc = "../../assets/restaurant.png";
            break;
        case '펜션':
            imageSrc = "../../assets/hotel.png";
            break;
        case '동물병원':
            imageSrc = "../../assets/hospital.png";
            break;
        case '동물약국':
            imageSrc = "../../assets/pharmacy.png";
            break;
        default:
    }
    return imageSrc;
};
const resultInsertUiHandler = async (searchType, data) => {

    const resultContainer = document.querySelector(".result-container");
    const modalContainerList = document.querySelector(".modal-container");
    const placeData = data.placeData;
    resultContainer.innerHTML = `
        <div class="wrapper animation-tag">
            <h3 class="result-title"># ${searchType} 검색 결과</h3>
            <p class="result-description"></p>
            <div class="result-list-wrapper"></div>
        </div>
    `
    const startCoordinate = data.coordinateRequest;

    let mapContainerBox = document.getElementById('map-wrapper');
    let mapContainer = document.getElementById("map");
    mapContainerBox.style.display="block";
    let options = {
        center: new kakao.maps.LatLng(startCoordinate.latitude, startCoordinate.longitude),
        level: 5
    };
    let map = new kakao.maps.Map(mapContainer, options);
    kakaoMap = map;
    let centerMarkerPosition  = new kakao.maps.LatLng(startCoordinate.latitude, startCoordinate.longitude);
    let marker = new kakao.maps.Marker({
        position: centerMarkerPosition
    });
    marker.setMap(map);

    const resultDescription = document.querySelector(".result-description");
    const resultListWrapper = document.querySelector(".result-list-wrapper");
    let listWrapperText = '';

    for(let i=0;i<placeData.length;i++){
        listWrapperText += `
             <p class="list-title">
                <i class="fa-solid fa-bookmark"></i> ${placeData[i].category}
            </p>
            <div class="place-list" id="place-list${i}"></div>
        `
    }
    resultListWrapper.innerHTML = listWrapperText;
    for(let i=0;i<placeData.length;i++){
        const imageSrc = getPlaceMarkerImageSrc(placeData[i].category);
        const placeList = placeData[i].placeList;

        const placeListDOM = document.getElementById(`place-list${i}`);

        let imageSize = new kakao.maps.Size(45, 45);
        let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
        for(let j=0;j<placeList.length;j++){
            let placeMarker = new kakao.maps.Marker({
                map: map,
                position:  new kakao.maps.LatLng(placeList[j].place.latitude, placeList[j].place.longitude),
                image : markerImage,
                clickable : true,
            });
            let content =`
                <div class="customOverlay">
                    <p class="customOverlay-placeName">${placeList[j].place.placeName}</p>
                    <p class="customOverlay-distance">${placeList[j].routeResponse.distance}</p>
                </div>
            `
            let customOverlay = new kakao.maps.CustomOverlay({
                position:  new kakao.maps.LatLng(placeList[j].place.latitude, placeList[j].place.longitude),
                content: content,
                yAnchor: 1,
            });
            kakao.maps.event.addListener(placeMarker, 'mouseover', makeOverListener(map, placeMarker, customOverlay));
            kakao.maps.event.addListener(placeMarker, 'mouseout', makeOutListener(customOverlay));
            kakao.maps.event.addListener(customOverlay, 'mouseover', makeOverListener(map, placeMarker, customOverlay));
            kakao.maps.event.addListener(customOverlay, 'mouseout', makeOutListener(customOverlay));
            kakao.maps.event.addListener(placeMarker, 'click', kakaoPlaceModalOpenHandler(placeList[j].place.id));


            const slng = startCoordinate.longitude;
            const slat = startCoordinate.latitude;

            const elng = placeList[j].place.longitude;
            const elat = placeList[j].place.latitude;
            const etext = placeList[j].place.placeName;

            let cardContainer = document.createElement("div");
            let modalContainer = document.createElement("div");
            let placeModalContainer = document.createElement("div");

            cardContainer.classList.add('place-card-wrapper');
            let ratingText;
            if (placeList[j].place.rating === 0){
                ratingText = 'NaN';
            }else{
                ratingText = (placeList[j].place.rating / placeList[j].place.reviewCount).toFixed(1);
            }
            cardContainer.innerHTML = `
                <h5 class="place-title"># ${placeList[j].place.placeName}</h5>
                 <div class="distance-wrapper">
                    <p>
                        ${placeList[j].routeResponse.distance}
                    </p>
                </div>
                <div class="front">
                    <div class="place-review-wrapper">
                        <p>
                            <i class="fa-solid fa-star" style="color:#ffdd11;"></i> ${ratingText} / 5.0
                        </p>
                        <p class="review-margin">
                            ${placeList[j].place.reviewCount}개 사용자 리뷰
                        </p>
                    </div>
                    <ul class="place-detail-wrapper">
                        <li class="place-detail">
                            <p><i class="fa-solid fa-location-dot"></i> 주소</p>
                            <span>${placeList[j].place.address}</span>
                        </li>
                        <li class="place-detail">
                            <p><i class="fa-solid fa-clock"></i> 운영시간</p>
                            <span>${placeList[j].place.operatingInfo}</span>
                        </li>
                        <li class="place-detail">
                            <p><i class="fa-regular fa-clock"></i> 휴뮤일</p>
                            <span>${placeList[j].place.holidayInfo}</span>
                        </li>
                        <li class="place-detail">
                            <p><i class="fa-solid fa-phone-volume"></i> 연락처</p>
                            <span>${placeList[j].place.phoneNumber}</span>
                        </li>
                    </ul>
                    <div class="button-container">
                        <button class="traffic-button" 
                       
                            data-bs-toggle="modal" 
                            data-bs-target="#traffic-modal${placeList[j].place.id}">
                                <i class="fa-solid fa-car"></i> 교통정보
                        </button>
                        <button class="place-button" 
                            id="place-button${placeList[j].place.id}"
                            data-bs-toggle="modal" 
                            onclick="fetchRecommendModal(${placeList[j].place.id})"
                            data-bs-target="#recommend-modal">
                                <i class="fa-solid fa-location-arrow"></i> 장소정보
                        </button>
                    </div>
                </div>
            `;
            modalContainer.innerHTML = `
                 <div class="modal fade" id="traffic-modal${placeList[j].place.id}" tabindex="-1" aria-labelledby="traffic-modal" aria-hidden="true">
                    <div class="modal-dialog  modal-dialog-centered traffic-modal">
                        <div class="modal-content">
                            <div class="modal-header traffic-header">
                                <h1 class="modal-title fs-5"># ${placeList[j].place.placeName}</h1>
                            </div>
                            <div class="modal-body">
                                <div class="back">
                                    <ul class="place-detail-wrapper">
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 총 거리 </p>
                                            <span>${placeList[j].routeResponse.distance}</span>
                                        </li>
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 차량 소요 시간</p>
                                            <span>${placeList[j].routeResponse.time}</span>
                                        </li>
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 택시 예상 요금</p>
                                            <span>${placeList[j].routeResponse.taxiFare}</span>
                                        </li>
                                        <li class="traffic-detail">
                                            <p><i class="fa-solid fa-square-check"></i> 톨게이트 예상 요금</p>
                                            <span>${placeList[j].routeResponse.tollFare}</span>
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
            placeModalContainer.innerHTML = `
              <div class="modal fade" id="place-modal${placeList[j].place.id}" tabindex="-1" aria-labelledby="traffic-modal" aria-hidden="true">
                    <div class="modal-dialog  modal-dialog-centered place-modal">
                        <div class="modal-content">
                            <div class="modal-header traffic-header">
                                <h1 class="modal-title fs-5"># ${placeList[j].place.placeName}</h1>
                                <div onclick="toggleStarFunction(this,${placeList[j].place.id})" id="star${placeList[j].place.id}" class="star-container"></div>
                            </div>
                            <div class="modal-body">
                                <div class="info-body">
                                    <div class="main-body">
                                        <p class="body-title">장소 기본정보</p>
                                        <li class="place-info">
                                            <p><i class="fa-solid fa-location-dot"></i> 주소</p>
                                            <span>${placeList[j].place.address}</span>
                                        </li>
                                        <li class="place-info">
                                            <p><i class="fa-solid fa-clock"></i> 운영시간</p>
                                            <span>${placeList[j].place.operatingInfo}</span>
                                        </li>
                                        <li class="place-info">
                                            <p><i class="fa-regular fa-clock"></i> 휴뮤일</p>
                                            <span>${placeList[j].place.holidayInfo}</span>
                                        </li>
                                        <li class="place-info">
                                            <p><i class="fa-solid fa-phone-volume"></i> 연락처</p>
                                            <span>${placeList[j].place.phoneNumber}</span>
                                        </li>
                                        <li class="place-info">
                                            <p><i class="fa-solid fa-square-parking"></i> 주차 가능여부</p>
                                            <span>${placeList[j].place.isParking}</span>
                                        </li>
                                    </div>
                                    <hr>
                                    <div id="detail-body${placeList[j].place.id}" class="detail-body">
                                        <p class="body-title">장소 세부정보</p>
                                        
                                        <li class="place-info detail">
                                            <p><i class="fa-solid fa-square-check"></i> 입장(이용료)가격 정보</p>
                                            <span>${placeList[j].place.feeInfo}</span>
                                        </li>
                                        <li class="place-info detail">
                                            <p><i class="fa-solid fa-square-check"></i> 애견 동반 추가 요금</p>
                                            <span>${placeList[j].place.addFeeInfo}</span>
                                        </li>
                                        <li class="place-info detail">
                                            <p><i class="fa-solid fa-square-check"></i> 장소(실외)여부</p>
                                            <span>${placeList[j].place.isOutside}</span>
                                        </li>
                                        <li class="place-info detail">
                                            <p><i class="fa-solid fa-square-check"></i> 장소(실내)여부</p>
                                            <span>${placeList[j].place.isInside}</span>
                                        </li>
                                         <li class="place-info detail">
                                            <p><i class="fa-solid fa-square-check"></i> 제한사항</p>
                                            <span>${placeList[j].place.restrictionInfo}</span>
                                        </li>
                                        <li class="place-info detail">
                                            <p><i class="fa-solid fa-square-check"></i> 입장 가능 동물 크기</p>
                                            <span>${placeList[j].place.maxSizeInfo}</span>
                                        </li>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `
            placeListDOM.appendChild(cardContainer);
            modalContainerList.appendChild(modalContainer);
            modalContainerList.appendChild(placeModalContainer);
            if (placeList[j].place.placeURL !== "정보없음"){
                const homepageButton = document.createElement('div');
                homepageButton.innerHTML = `
                    <li class="place-info detail">
                        <p>
                            <i class="fa-solid fa-location-arrow"></i>
                             <a target="_blank" href="${placeList[j].place.placeURL.startsWith('http') ? placeList[j].place.placeURL : 'https://' + placeList[j].place.placeURL}">
                                홈페이지 바로가기
                            </a>
                        </p>
                    </li>
                `
                const targetBox = document.querySelector(`#detail-body${placeList[j].place.id}`);
                targetBox.appendChild(homepageButton);
            }
            const isFavoriteResponseData = await isFavoriteProcess(placeList[j].place.id);
            const targetStar = document.getElementById(`star${placeList[j].place.id}`);
            if (isFavoriteResponseData){
                targetStar.innerHTML = `<i itemid=${isFavoriteResponseData} class=\"fa-solid fa-star\"></i>`;
            }else{
                targetStar.innerHTML = "<i itemid='0' class=\"fa-regular fa-star\"></i>";
            }
        }

    }
    switch(type) {
        case 'care':
            resultDescription.innerHTML = `
                반려동물 케어 장소에 대한 검색 결과입니다. <br> 
                근처 반려동물용품, 미용 및 위탁관리에 대한 데이터는 각각 5개씩 제공됩니다. 교통정보도 확인해보세요! <br>
            `
            break;
        case 'tour':
            resultDescription.innerHTML = `
                박물관 및 여행지에 대한 검색 결과입니다. <br> 
                근처 박물관 및 여행지에 대한 데이터는 각각 5개씩 제공됩니다. 교통정보도 확인해보세요! <br>
            `
            break;
        case 'hotel':
            resultDescription.innerHTML = `
                식당 및 펜션에 대한 검색 결과입니다. <br> 
                근처 식당 및 펜션에 대한 데이터는 각각 5개씩 제공됩니다. 교통정보도 확인해보세요! <br>
            `
            break;
        case 'hospital':
            resultDescription.innerHTML = `
                동물병원 동물약국에 대한 검색 결과입니다. <br> 
                근처 병원과 약국에 대한 데이터는 각각 5개씩 제공됩니다. 교통정보도 확인해보세요! <br>
            `
            break;
        default:
    }
    loadingModal.style.display="none";
};
const resultContainerHandler = async (searchType,data) => {

    let innerText = "";
    const resultContainer = document.querySelector(".result-container");

    // 모든 교통정보 삭제
    const trafficModals = document.querySelectorAll('[id^="traffic-modal"]');
    trafficModals.forEach(item => {
        item.remove();
    })
    // 모든 장소 정보 삭제
    const placeModals = document.querySelectorAll('[id^="place-modal"]');
    placeModals.forEach(item => {
        item.remove();
    })

    if (searchType !== "장소 키워드"){
        await resultInsertUiHandler(searchType,data);
    }
    if (searchType === "장소 키워드"){
        innerText += `
            <div class="wrapper animation-tag">
                <h3 class="result-title"># ${searchType} 검색 결과</h3>
                <p class="result-description">
                    해당 카테고리 및 정보에 대한 키워드 검색 결과입니다. <br> 
                    총 ${data.placeList.length}건의 결과가 존재합니다. <br>
                </p>
            </div>
        `
        if (data.placeList.length === 0){
            innerText += `
                <p class="no-message">※ 검색된 결과가 없습니다. 다른 카테고리 및 필터를 적용시켜주세요!</p>
            `
        }else{
            innerText += `
                <div class="search-list-wrapper">
                    <p class="search-title"><i class="fa-solid fa-bookmark"></i> 검색 결과</p>
                    <div class="place-list">
                        <table class="place-table table table-bordered table-hover">
                             <thead class="table-head">
                                <tr>
                                    <th scope="col" style="width:5%;">No</th>
                                    <th scope="col" style="width:10%;">카테고리</th>
                                    <th scope="col" style="width:15%;">장소명</th>
                                    <th scope="col" style="width:30%;">주소</th>
                                    <th scope="col" style="width:25%;">운영시간</th>
                                    <th scope="col" style="width:15%;">휴무일</th>
                                </tr>
                            </thead>
                            <tbody class="table-body"></tbody>
                        </table>
                    </div>
                </div>
            `
        }
        resultContainer.innerHTML = innerText;
        const bodyTag = document.querySelector(".table-body");

        for(let i=0;i<data.placeList.length;i++) {
            let placeModalContainer = document.createElement("div");
            const rowTag = document.createElement('tr');
            rowTag.classList.add("place-tr");
            rowTag.setAttribute("data-bs-toggle","modal");
            rowTag.setAttribute("data-bs-target",`#recommend-modal`);
            rowTag.setAttribute("onclick",`fetchRecommendModal(${data.placeList[i].id})`);

            rowTag.innerHTML = `
                <th scope="row" class="text-center text_number">${i+1}</th>
                <td class="text-center text_category">${data.placeList[i].category}</td>
                <td class="text-center">${data.placeList[i].placeName}</td>
                <td class="text-center">${data.placeList[i].address}</td>
                <td class="text-center">${data.placeList[i].operatingInfo}</td>
                <td class="text-center">${data.placeList[i].holidayInfo}</td>
            `
            bodyTag.appendChild(rowTag);
        }
    }

};
const toggleStarFunction = async (element,placeId) => {

    const isFavorite = element.firstChild.getAttribute('itemid');
    const itemType = element.getAttribute('itemtype');
    if (isFavorite === '0'){
        const addResponseData = await addFavoriteProcess(placeId);
        if (addResponseData){
            const isFavoriteResponseData = await isFavoriteProcess(placeId);
            element.innerHTML = `<i itemid=${isFavoriteResponseData} class=\"fa-solid fa-star\"></i>`
            if (itemType === 'recommend'){
                if (document.getElementById(`star${placeId}`)){
                    const targetElement = document.getElementById(`star${placeId}`);
                    targetElement.innerHTML = `<i itemid=${isFavoriteResponseData} class=\"fa-solid fa-star\"></i>`;
                }
            }
        }
    }else{
        const deleteResponseData = await deleteFavoriteProcess(isFavorite);
        if (deleteResponseData){
            element.innerHTML = `<i itemid='0' class="fa-regular fa-star"></i>`
            if (itemType === 'recommend'){
                if (document.getElementById(`star${placeId}`)){
                    const targetElement = document.getElementById(`star${placeId}`);
                    targetElement.innerHTML =  `<i itemid='0' class="fa-regular fa-star"></i>`;
                }
            }
        }
    }
};
const searchPlaceHandler = async () => {

    const placeNameValue = document.querySelector("#placeName").value;
    const isParkingValue = document.querySelector("#isParking").checked;
    const isInsideValue = document.querySelector("#isInside").checked;
    const isOutsideValue = document.querySelector("#isOutside").checked;
    const categoryValue = document.querySelector("#category").value;
    const regionCodeValue = document.querySelector("#region").value;

    if (categoryValue === "카테고리 선택"){
        swal(
            'Warning!',
            '카테고리 선택은 필수입니다. <br/> 다시 시도해주세요!',
            'warning'
        )
        return;
    }
    document.querySelector("#placeName").value = "";
    document.querySelector("#isParking").checked = false;
    document.querySelector("#isInside").checked = false;
    document.querySelector("#isOutside").checked = false;
    document.querySelector(".form-select").value = "카테고리 선택";
    document.querySelector("#region").value = "지역 선택";

    const keywordPlaceList = await searchByKeywordProcess({
        placeName : placeNameValue,
        isParking : isParkingValue,
        isInside : isInsideValue,
        isOutside : isOutsideValue,
        category : categoryValue,
        regionCode : regionCodeValue,
    });
    document.querySelector("#map-wrapper").style.display="none";
    searchModal.hide();
    loadingModal.style.display="block";
    setTimeout(() => {
        loadingModal.style.display = "none";
    }, 7000);
    await resultContainerHandler("장소 키워드",{
        placeList : keywordPlaceList
    });

};
const addressToCoordinate = async (address) => {
    const coordinateResponseData = await addressToCoordinateProcess(address);
    if (coordinateResponseData){
        await getAroundAddressPlaceList(coordinateResponseData);
    }
};
const getPlaceListByType = async (coordinateRequest) => {
    const dataList = [];
    switch(type) {
        case 'care':
            dataList.push({
                category: "반려동물용품",
                placeList: await getAroundPlaceProcess(coordinateRequest, "반려동물용품")
            });
            dataList.push({
                category: "미용",
                placeList: await getAroundPlaceProcess(coordinateRequest, "미용")
            });
            dataList.push({
                category: "위탁관리",
                placeList: await getAroundPlaceProcess(coordinateRequest, "위탁관리")
            });
            break;
        case 'tour':
            dataList.push({
                category: "문예회관",
                placeList: await getAroundPlaceProcess(coordinateRequest, "문예회관")
            });
            dataList.push({
                category: "미술관",
                placeList: await getAroundPlaceProcess(coordinateRequest, "미술관")
            });
            dataList.push({
                category: "박물관",
                placeList: await getAroundPlaceProcess(coordinateRequest, "박물관")
            });
            dataList.push({
                category: "여행지",
                placeList: await getAroundPlaceProcess(coordinateRequest, "여행지")
            });
            break;
        case 'hotel':
            dataList.push({
                category: "카페",
                placeList: await getAroundPlaceProcess(coordinateRequest, "카페")
            });
            dataList.push({
                category: "식당",
                placeList: await getAroundPlaceProcess(coordinateRequest, "식당")
            });
            dataList.push({
                category: "펜션",
                placeList: await getAroundPlaceProcess(coordinateRequest, "펜션")
            });
            break;
        case 'hospital':
            dataList.push({
                category: "동물병원",
                placeList: await getAroundPlaceProcess(coordinateRequest, "동물병원")
            });
            dataList.push({
                category: "동물약국",
                placeList: await getAroundPlaceProcess(coordinateRequest, "동물약국")
            });
            break;
        default:
    }
    return dataList;
}
const getCurrentPlaceList = async (position) => {
    const coordinateRequest = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
    }
    const placeData = await getPlaceListByType(coordinateRequest);
    await resultContainerHandler("현재 위치",{
        placeData,
        coordinateRequest
    });
};

const getAroundAddressPlaceList = async (coordinateRequest) => {
    const placeData = await getPlaceListByType(coordinateRequest);
    await resultContainerHandler("주소",{
        placeData,
        coordinateRequest
    });
};
async function aiRequest(event) {
    if (event.key === 'Enter') {
        const userQuestion = document.getElementById('gemini-input').value;
        if (userQuestion.trim().length === 0){
            event.preventDefault();
            swal(
                'Warning!!',
                '질문 내용을 입력해주세요!',
                'warning'
            );
            return;
        }
        document.querySelector('.loading-container').style.display = "block";
        document.getElementById('gemini-input').value = '';
        document.getElementById('gemini-input').blur();
        resizeTextarea(document.getElementById('gemini-input'));

        const aiQuestionResponseData = await AIQuestionProcess(userQuestion);
        if (aiQuestionResponseData){
            insertAIResponse(userQuestion, aiQuestionResponseData.toString());
        }

    }
}
function insertAIResponse(question,response){
    const container = document.querySelector(".ai-response-container");

    const wrapperBox = document.createElement('div');
    wrapperBox.classList.add('ai-wrapper');

    const questionContainer = document.createElement("div");
    questionContainer.classList.add('question-container');
    questionContainer.innerHTML = `
        <p class="question-text">${question}</p>
    `

    const answerContainer = document.createElement('div');
    answerContainer.classList.add('answer-container');
    const markDownResponse = markdownToHTML(response);
    answerContainer.innerHTML = `
        <p class="answer"><i style="margin-right: 10px;font-size:20px" class="fa-brands fa-square-google-plus"></i> Gemini Answer</p>
        ${markDownResponse}
    `
    wrapperBox.appendChild(questionContainer);
    wrapperBox.appendChild(answerContainer);
    container.appendChild(wrapperBox);
    document.querySelector('.loading-container').style.display = "none";
}
function markdownToHTML(markdown) {

    let html = markdown
        .replace(/(^|\n)###\s*(.+)/g, '$1<h3>$2</h3>')
        .replace(/(^|\n)##\s*(.+)/g, '$1<h2>$2</h2>')
        .replace(/(^|\n)#\s*(.+)/g, '$1<h1>$2</h1>')
        .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.+?)\*/g, '<em>$1</em>');
    html = html.replace(/(^|\n)\*\s*(.+)/g, '$1- $2')
        .replace(/(^|\n)-\s*(.+)/g, '$1<li>$2</li>');
    html = html.replace(/(<li>.*<\/li>)/gs, '<ul>$1</ul>');
    html = html.replace(/<\/ul><ul>/g, '');
    html = html.replace(/\n/g, '<br>');


    html = `<div class="answer-text">${html}</div>`;
    return html;
}
function resizeTextarea(element) {
    element.style.height = 'auto';
    element.style.height = (element.scrollHeight) + 'px';
}
const getRecommendPlaceList = async (position) => {
    loadingModal.style.display="block";
    const coordinateRequest = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
    }
    const placeData = await getRecommendPlaceProcess(coordinateRequest);
    recommendPlaceHandler(placeData,coordinateRequest);
};
const fetchRecommendModal = async (placeId) => {

    const modalHeader = document.querySelector('#recommend-modal .modal-header');
    const modalBody =  document.querySelector('#recommend-modal .modal-body');

    loadingModal.style.display="block";
    const allReviewItems = await getAllReviewByPlaceIdProcess(placeId);
    const targetPlace = await getPlaceByIdProcess(placeId);

    modalHeader.innerHTML = `
        <h1 class="modal-title fs-5"># ${targetPlace.placeName}</h1>
        <div 
            itemtype="recommend"
            onclick="toggleStarFunction(this,${targetPlace.id})" 
            id="recommendStar${targetPlace.id}" class="star-container"></div>
    `
    modalBody.innerHTML = `
        <div class="info-body">
            <div class="main-body">
                <p class="body-title">장소 기본정보</p>
                <li class="place-info">
                    <p><i class="fa-solid fa-location-dot"></i> 주소</p>
                    <span>${targetPlace.address}</span>
                </li>
                <li class="place-info">
                    <p><i class="fa-solid fa-clock"></i> 운영시간</p>
                    <span>${targetPlace.operatingInfo}</span>
                </li>
                <li class="place-info">
                    <p><i class="fa-regular fa-clock"></i> 휴뮤일</p>
                    <span>${targetPlace.holidayInfo}</span>
                </li>
                <li class="place-info">
                    <p><i class="fa-solid fa-phone-volume"></i> 연락처</p>
                    <span>${targetPlace.phoneNumber}</span>
                </li>
                <li class="place-info">
                    <p><i class="fa-solid fa-square-parking"></i> 주차 가능여부</p>
                    <span>${targetPlace.isParking}</span>
                </li>
            </div>
            <hr>
            <div id="detail-body${targetPlace.id}" class="detail-body">
                <p class="body-title">장소 세부정보</p>
                
                <li class="place-info detail">
                    <p><i class="fa-solid fa-square-check"></i> 입장(이용료)가격 정보</p>
                    <span>${targetPlace.feeInfo}</span>
                </li>
                <li class="place-info detail">
                    <p><i class="fa-solid fa-square-check"></i> 애견 동반 추가 요금</p>
                    <span>${targetPlace.addFeeInfo}</span>
                </li>
                <li class="place-info detail">
                    <p><i class="fa-solid fa-square-check"></i> 장소(실외)여부</p>
                    <span>${targetPlace.isOutside}</span>
                </li>
                <li class="place-info detail">
                    <p><i class="fa-solid fa-square-check"></i> 장소(실내)여부</p>
                    <span>${targetPlace.isInside}</span>
                </li>
                 <li class="place-info detail">
                    <p><i class="fa-solid fa-square-check"></i> 제한사항</p>
                    <span>${targetPlace.restrictionInfo}</span>
                </li>
                <li class="place-info detail">
                    <p><i class="fa-solid fa-square-check"></i> 입장 가능 동물 크기</p>
                    <span>${targetPlace.maxSizeInfo}</span>
                </li>
            </div>
        </div>
        <div class="review-desc"></div>
        <p class="no-message" style="display: none"></p>
        <ul class="review-list"></ul>
    `
    const isFavoriteResponseData = await isFavoriteProcess(targetPlace.id);
    const targetStar = document.getElementById(`recommendStar${targetPlace.id}`);
    if (isFavoriteResponseData){
        targetStar.innerHTML = `<i itemid=${isFavoriteResponseData} class=\"fa-solid fa-star\"></i>`;
    }else{
        targetStar.innerHTML = "<i itemid='0' class=\"fa-regular fa-star\"></i>";
    }

    const newDataArray = [];
    for(let i=0;i<allReviewItems.length;i++){
        const reviewId = allReviewItems[i].id;
        const getAllFileList = await getReviewFileProcess(reviewId);
        const imageList = [];

        for (let i = 0; i < getAllFileList.length; i++) {
            const fileData = getAllFileList[i];
            const base64Data = fileData.imageData;
            const mimeType = fileData.mimeType;

            const byteCharacters = atob(base64Data);
            const byteNumbers = new Array(byteCharacters.length);
            for (let j = 0; j < byteCharacters.length; j++) {
                byteNumbers[j] = byteCharacters.charCodeAt(j);
            }
            const byteArray = new Uint8Array(byteNumbers);
            const blob = new Blob([byteArray], { type: mimeType });
            const imageUrl = URL.createObjectURL(blob);
            imageList.push(imageUrl);
        }
        newDataArray.push({
            ...allReviewItems[i],
            imageList
        })
    }
    fetchRecommendModalUI(modalBody,newDataArray,targetPlace);

};
const fetchRecommendModalUI = (element,reviewData,placeData) => {

    const noMessageBox = document.querySelector('#recommend-modal .no-message');
    const headerBox = document.querySelector('#recommend-modal .review-desc');
    // 이전의 값 초기화
    noMessageBox.innerHTML = '';
    const reviewSize = placeData.reviewCount;
    let reviewScore = 'NaN';
    if (reviewSize !== 0){
        reviewScore = (placeData.rating / reviewSize).toFixed(1);
    }
    headerBox.innerHTML = `
         <p class="review-recommend-title"># 사용자 리뷰</p>
         <div class="review-desc-wrapper">
            <p>
                <i class="fa-solid fa-star" style="color:red;"></i> ${reviewScore}
            </p>
            <p class="review-count">
                · ${reviewSize}개 사용자 평점 및 리뷰
            </p>
         </div>
    `
    const reviewList = document.querySelector("#recommend-modal .review-list");
    while (reviewList.firstChild) {
        reviewList.removeChild(reviewList.firstChild);
    }
    if (reviewData.length === 0){
        noMessageBox.style.display='block';
        noMessageBox.innerHTML = `아직 등록된 리뷰가 없습니다. <br> 리뷰를 등록해주세요!`
    }else{
        noMessageBox.style.display='none';
        for(let i=0;i<reviewData.length;i++){
            const reviewItem = document.createElement('li');
            reviewItem.classList.add('review-list-item');
            reviewItem.innerHTML = `
                <div class="review-item-desc">
                    <p>
                        <i class="fa-solid fa-star" style="color:#ffdd11;"></i> 
                        <span style="color:black">${reviewData[i].rating.toFixed(1)}</span> / 5.0 
                    </p>
                    <p> · ${reviewData[i].member.name}</p>
                    <p> · ${reviewData[i].createdDateTime.split("T")[0]}</p>
                    <p> · 사진 ${reviewData[i].imageList.length}개 등록</p>
                </div>
                <div class="review-image-list" id="review-recommend-image-list${reviewData[i].id}"></div>
                <p class="review-text">
                    ${reviewData[i].text}
                </p>
            `
            reviewList.append(reviewItem);
            const imageListTag = document.querySelector(`#review-recommend-image-list${reviewData[i].id}`);
            if(reviewData[i].imageList.length === 0){
                imageListTag.style.display = 'none';
            }
            for(let j=0;j<reviewData[i].imageList.length;j++){
                const imageTag = document.createElement('div');
                imageTag.innerHTML = `
                    <img src=${reviewData[i].imageList[j]} alt="review-image" class="review-image"/>
                `
                imageTag.querySelector('img').addEventListener('click', () => openImageModal(reviewData[i].imageList[j],reviewData[i],reviewData[i].imageList));
                imageListTag.append(imageTag);
            }

        }
    }
    loadingModal.style.display="none";
}
const recommendPlaceHandler = (placeData) => {
    const distListWrapper = document.querySelector(".dist-list");
    const userListWrapper = document.querySelector(".user-list");

    const distBasedList = placeData.distBasedList;
    let itemBasedList = placeData.itemBasedList;

    let distText='';
    let itemText = '';
    for(let i=0;i<distBasedList.length;i++){
        let ratingText;
        if (distBasedList[i].place.rating === 0){
            ratingText = 'NaN';
        }else{
            ratingText = (distBasedList[i].place.rating / distBasedList[i].place.reviewCount).toFixed(1);
        }
        distText += `
            <div class="near-card animation" 
                data-bs-toggle="modal"
                data-bs-target="#recommend-modal"
                onclick="fetchRecommendModal(${distBasedList[i].place.id})">
                <div class="card-front">
                    <div class="large"># ${distBasedList[i].place.placeName} </div>
                    <div class="place-review-wrapper">
                        <p>
                            <i class="fa-solid fa-star" style="color:#ffdd11;"></i> ${ratingText} / 5.0
                        </p>
                        <p class="review-margin">
                            ${distBasedList[i].place.reviewCount}개 사용자 리뷰
                        </p>
                    </div>
                    <div class="front_section">
                        <ul class="place-detail-wrapper">
                            <li class="place-detail">
                                <p><i class="fa-solid fa-location-dot"></i> 주소</p>
                                <span>${distBasedList[i].place.address}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-clock"></i> 운영시간</p>
                                <span>${distBasedList[i].place.operatingInfo}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-regular fa-clock"></i> 휴뮤일</p>
                                <span>${distBasedList[i].place.holidayInfo}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-phone-volume"></i> 연락처</p>
                                <span>${distBasedList[i].place.phoneNumber}</span>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="card-back">
                    <div class="back_large"> # ${distBasedList[i].place.placeName} </div>
                    <p class="desc"><i class="fa-solid fa-car"></i> 장소 교통정보</p>
                    <div class="back_section">
                        <ul class="place-detail-wrapper">
                            <li class="place-detail">
                                <p><i class="fa-solid fa-square-check"></i> 총 거리 </p>
                                <span>${distBasedList[i].routeResponse.distance}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-square-check"></i> 차량 소요 시간</p>
                                <span>${distBasedList[i].routeResponse.time}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-square-check"></i> 택시 예상 요금</p>
                                <span>${distBasedList[i].routeResponse.taxiFare}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-square-check"></i> 톨게이트 예상 요금</p>
                                <span>${distBasedList[i].routeResponse.tollFare}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>      
        `;
    }
    distListWrapper.innerHTML = distText;
    if (itemBasedList.length === 0){
        itemText += `
            <p class="no-item-message">
                사용자의 데이터가 부족합니다. <br>
                리뷰 및 즐겨찾기 등록을 통해 장소를 추천받아 보세요!
            </p>
        `
    }else{
        for(let i=0;i<itemBasedList.length;i++){
            let score = (itemBasedList[i].score * 20).toFixed(1);
            let ratingText;
            if (itemBasedList[i].place.rating === 0){
                ratingText = 'NaN';
            }else{
                ratingText = (itemBasedList[i].place.rating / itemBasedList[i].place.reviewCount).toFixed(1);
            }
            itemText += `
                <div class="main-card"
                    data-bs-toggle="modal"
                    data-bs-target="#recommend-modal"
                    onclick="fetchRecommendModal(${itemBasedList[i].place.id})"
                    >
                    <h5 class="main-card-title"># ${itemBasedList[i].place.placeName}</h5>
                    <div class="place-review-wrapper">
                        <p>
                            <i class="fa-solid fa-star" style="color:#ffdd11;"></i> ${ratingText} / 5.0
                        </p>
                        <p class="review-margin">
                            ${itemBasedList[i].place.reviewCount}개 사용자 리뷰
                        </p>
                    </div>
                    <p class="recommend-score">
                        · 만족도 ${score}% 예상
                    </p>
                    <div class="main-front-section">
                        <ul class="place-detail-wrapper">
                            <li class="place-detail">
                                <p><i class="fa-solid fa-location-dot"></i> 주소</p>
                                <span>${itemBasedList[i].place.address}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-clock"></i> 운영시간</p>
                                <span>${itemBasedList[i].place.operatingInfo}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-regular fa-clock"></i> 휴뮤일</p>
                                <span>${itemBasedList[i].place.holidayInfo}</span>
                            </li>
                            <li class="place-detail">
                                <p><i class="fa-solid fa-phone-volume"></i> 연락처</p>
                                <span>${itemBasedList[i].place.phoneNumber}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            `
        }
    }
    userListWrapper.innerHTML = itemText;
    loadingModal.style.display="none";
};
getForCurrentLocation(getRecommendPlaceList);