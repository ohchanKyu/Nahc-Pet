const naverMapURL = 'http://map.naver.com/index.nhn?';
const loadingModal = document.querySelector(".loading-modal");

const deleteFavoriteHandler =  async (favoriteId) => {
    const deleteResponseData = await deleteFavoriteProcess(favoriteId);
    if (deleteResponseData){
        swal(
            'Success!',
            '즐겨찾기 삭제가 완료되었습니다!',
            'success'
        ).then(result => {
            if (result.value) {
                location.href = `/private/me/favorite`;
            }
        })
    }
};

getForCurrentLocation(initializeFavoriteArray);

async function initializeFavoriteArray(position){

    loadingModal.style.display="block";
    const coordinateRequest = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
    }
    const modalContainer = document.querySelector(".modal-container");

    for(let i=0;i<favoritePlaceData.length;i++){

        const slng = coordinateRequest.longitude;
        const slat = coordinateRequest.latitude;

        const elng = favoritePlaceData[i].place.longitude;
        const elat = favoritePlaceData[i].place.latitude;
        const etext = favoritePlaceData[i].place.placeName;

        const routeData = await getTrafficInformationProcess({
            startLatitude : coordinateRequest.latitude,
            startLongitude : coordinateRequest.longitude,
            endLatitude : favoritePlaceData[i].place.latitude,
            endLongitude : favoritePlaceData[i].place.longitude,
        });
        const modalBox = document.createElement('div');
        modalBox.innerHTML = `
            <div class="modal fade" id="traffic-modal${favoritePlaceData[i].place.id}" tabindex="-1" aria-labelledby="traffic-modal" aria-hidden="true">
            <div class="modal-dialog  modal-dialog-centered traffic-modal">
                <div class="modal-content">
                    <div class="modal-header traffic-header">
                        <h1 class="modal-title fs-5"># ${favoritePlaceData[i].place.placeName}</h1>
                    </div>
                    <div class="modal-body">
                        <div class="back">
                            <ul class="place-detail-wrapper">
                                <li class="traffic-detail">
                                    <p><i class="fa-solid fa-square-check"></i> 총 거리 </p>
                                    <span>${routeData.distance}</span>
                                </li>
                                <li class="traffic-detail">
                                    <p><i class="fa-solid fa-square-check"></i> 차량 소요 시간</p>
                                    <span>${routeData.time}</span>
                                </li>
                                <li class="traffic-detail">
                                    <p><i class="fa-solid fa-square-check"></i> 택시 예상 요금</p>
                                    <span>${routeData.taxiFare}</span>
                                </li>
                                <li class="traffic-detail">
                                    <p><i class="fa-solid fa-square-check"></i> 톨게이트 예상 요금</p>
                                    <span>${routeData.tollFare}</span>
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
        `
        modalContainer.appendChild(modalBox);
    }
    const allCardArray = document.querySelectorAll(".wrapper");
    const allListWrapper = document.querySelector(".favorite-list-wrapper");

    const groupedArray = [];
    for (let i = 0; i < allCardArray.length; i += 2) {
        groupedArray.push(Array.from(allCardArray).slice(i, i + 2));
    }
    for(let i=0;i<groupedArray.length;i++){
        const flexBox = document.createElement('div');
        flexBox.classList.add('flex-box');
        if (groupedArray[i].length === 2){
            flexBox.appendChild(groupedArray[i][0]);
            flexBox.appendChild(groupedArray[i][1]);
        }else{
            flexBox.appendChild(groupedArray[i][0]);
        }
        allListWrapper.appendChild(flexBox);
    }
    loadingModal.style.display="none";
}

if (document.querySelector("#email-save-button")){
    document.querySelector("#email-save-button").addEventListener("click",async function (evt){
        const emailValue = document.querySelector("#email").value;
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (emailValue.trim().length === 0){
            swal(
                'Warning!',
                '입력된 내용이 없습니다 <br/> 이메일을 입력해주세요.',
                'warning'
            )
            return;
        }
        if (!emailRegex.test(emailValue)) {
            swal(
                'Warning!',
                '잘못된 이메일 형식입니다.',
                'warning'
            )
            return;
        }
        const editEmailResponseData = await editEmailProcess(emailValue);
        if (editEmailResponseData){
            swal(
                'Success!',
                '회원님의 이메일을 변경하였습니다. <br> 다시 로그인해주세요!',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/auth/logout";
                }
            })
        }else{
            swal(
                'Error!',
                '이메일이 중복됩니다. <br/> 다시 시도해주세요!',
                'error'
            )
        }
    })

    document.querySelector("#name-save-button").addEventListener("click",async function (evt){
        const nameValue = document.querySelector("#name").value;
        if (nameValue.trim().length === 0){
            swal(
                'Warning!',
                '입력된 내용이 없습니다 <br/> 이름을 입력해주세요.',
                'warning'
            )
            return;
        }
        const editNameResponseData = await editNameProcess(nameValue);
        if (editNameResponseData){
            swal(
                'Success!',
                '회원님의 이름을 변경하였습니다.',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/private/me/user";
                }
            })
        }
    })

    document.querySelector("#delete-user-button").addEventListener("click",async function (evt){

        const deleteMemberResponseData = await deleteMemberProcess();
        if (deleteMemberResponseData){
            swal(
                'Success!',
                '회원탈퇴를 성공하셨습니다. <br/> 메인 페이지로 이동합니다.',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/auth/logout";
                }
            })
        }
    });
}
let currentStars;
let currentRating = 0;
let currentPlaceId;
const maxFileNumber = 3;
let fileId = 0;
let filesArray = [];

const initializeReview = async (placeId) => {

    const isCheckData = await checkIsAlreadyProcess(placeId);
    // 리뷰 장소 이름 변경
    document.querySelector("#review-modal .modal-title").innerHTML =  document.querySelector(`#place-modal${placeId} .modal-title`).innerHTML;
    // 장소 아이디 초기화
    currentPlaceId = placeId;
    // 별점 초기화
    currentStars = document.querySelectorAll(`.rating .star-icon`);
    const targetButton = document.querySelector('#review-button');
    // 글자수
    let countTag = document.querySelector(`.guide_review`).firstElementChild;
    // 파일 담는거 초기화
    fileId = 0;
    filesArray = [];
    let fileBody = document.querySelector(".file-list");
    while (fileBody.firstChild) {
        fileBody.removeChild(fileBody.firstChild);
    }
    if (isCheckData){
        const getAllFileList = await getReviewFileProcess(isCheckData.id);
        console.log(getAllFileList);
        for(let i=0;i<getAllFileList.length;i++){
            editFileArray(getAllFileList[i]);
        }

        currentRating = isCheckData.rating;
        document.querySelector("#review-modal .modal-sub-title").innerHTML = '등록하신 리뷰를 수정가능합니다!';
        document.querySelector(`.review_textarea`).value = isCheckData.text;
        targetButton.innerHTML = "리뷰 수정";
        targetButton.dataset.reviewType = 'edit';
        targetButton.dataset.reviewId = isCheckData.id;
        countTag.innerHTML = isCheckData.text.length;
        initStars(currentStars);
        filledRate(currentStars, currentRating);
    }else{
        document.querySelector("#review-modal .modal-sub-title").innerHTML = '해당 장소에 대한 리뷰를 등록해보세요!';
        document.querySelector(`.review_textarea`).value = "";
        countTag.innerHTML = 0;
        currentRating = 0;
        initStars(currentStars);
        targetButton.innerHTML = "리뷰 등록";
        targetButton.dataset.reviewType = 'new';
    }
    const inputs = document.querySelectorAll(`.rating .rating__input`);

    $(`.review_textarea`).keyup(function(evt){
        let content = $(this).val();
        countTag.innerHTML = content.length;
    });

    inputs.forEach(input => {
        input.addEventListener('click', () => {
            const value = input.value;
            currentRating = value;
            initStars(currentStars);
            filledRate(currentStars, value);
        });
    });

    currentStars.forEach(star => {
        star.addEventListener('mouseenter', () => {
            const value = star.closest('label').querySelector('input').value;
            initStars(currentStars);
            filledRate(currentStars, value);
        });

        star.addEventListener('mouseleave', () => {
            initStars(currentStars);
            filledRate(currentStars, currentRating);
        });
    });
}
const addReviewHandler = async (element)=> {

    const reviewType = element.dataset.reviewType;
    const inputValue = document.querySelector(`.review_textarea`).value;
    const rating = currentRating;
    const formData = new FormData();
    for(let i=0;i<filesArray.length;i++){
        if (!filesArray[i].is_delete){
            formData.append("file",filesArray[i]);
        }
    }
    if (rating === 0){
        swal(
            'Warning!',
            '평점은 필수 선택사항입니다.',
            'warning'
        )
        return;
    }
    if (inputValue.trim().length < 5){
        swal(
            'Warning!',
            '5자 이상으로 리뷰를 입력해주세요!',
            'warning'
        )
        return;
    }
    const reviewData = {
        text : inputValue,
        rating : currentRating,
    }
    formData.append("review", new Blob([JSON.stringify(reviewData)], { type: 'application/json' }));

    if (reviewType === 'edit'){
        const reviewId = element.dataset.reviewId;
        const editReviewResponseData = await editOneReviewProcess(formData,reviewId);
        if (editReviewResponseData){
            swal(
                'Success!',
                '리뷰 수정이 완료되었습니다!',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = `/private/me/favorite`;
                }
            })
        }
    }else{
        const addReviewResponse = await addReviewProcess(formData,currentPlaceId);
        if (addReviewResponse){
            swal(
                'Success!',
                '리뷰 등록이 완료되었습니다!',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = `/private/me/favorite`;
                }
            })
        }
    }
}
const pushFileArray = (evt) => {
    // 그 전까지 등록한 파일 개수
    const previousFileNumber = document.querySelectorAll('.file-box').length;
    if (previousFileNumber >= maxFileNumber){
        swal(
            'Warning!',
            '첨부 파일은 최대 3개까지 입니다.',
            'warning'
        )
        return;
    }
    const file = evt.target.files[0];

    if (validation(file)) {
        // 파일 배열에 담기
        filesArray.push(file);
        let fileBody = document.querySelector(".file-list");
        let fileItem = document.createElement("div");

        fileItem.setAttribute("class",`file-box`);
        fileItem.setAttribute("id",`file${fileId}`);

        let reader = new FileReader();

        reader.onload = function(e) {
            file.src = e.target.result;  // 이미지 소스 설정
            fileItem.appendChild(file);  // 이미지 요소를 파일 아이템에 추가
        };
        let img = document.createElement("img");
        img.setAttribute("class", "thumbnail");
        img.src = URL.createObjectURL(file);
        fileItem.appendChild(img);
        fileItem.innerHTML += `<p class="delete" onclick="deleteFile(${fileId})">삭제 <i class="fa-regular fa-circle-xmark delete-mark"></i></p>`;
        fileBody.append(fileItem);
        fileId += 1;
    }
    evt.target.value = "";
};
const editFileArray = (file) => {
    const byteString = atob(file.imageData);
    const mimeType = file.mimeType;
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    const blob = new Blob([ab], { type: mimeType });
    const ext = mimeType.split("/")[1];
    const fileName = "existing_file"+`.${ext}`
    const newFile = new File([blob], fileName, { type: mimeType });

    filesArray.push(newFile);
    let fileBody = document.querySelector(".file-list");
    let fileItem = document.createElement("div");

    fileItem.setAttribute("class", `file-box`);
    fileItem.setAttribute("id", `file${fileId}`);

    let img = document.createElement("img");
    img.setAttribute("class", "thumbnail");
    img.src = URL.createObjectURL(newFile);
    fileItem.appendChild(img);
    fileItem.innerHTML += `<p class="delete" onclick="deleteFile(${fileId})">삭제 <i class="fa-regular fa-circle-xmark delete-mark"></i></p>`;
    fileBody.append(fileItem);
    fileId += 1;
};

if (document.querySelector("#file")){
    document.querySelector("#file").addEventListener("change",function(evt){
        pushFileArray(evt);
    });

    function validation(fileObject){
        const fileTypes = ['image/gif','image/jpg', 'image/jpeg', 'image/png'];
        if (fileObject.name.length > 100) {
            swal(
                'Warning!',
                '파일명이 100자 이상인 <br> 파일은 제외되었습니다.',
                'warning'
            )
            return false;
        } else if (fileObject.size > 1048576) {
            swal(
                'Warning!',
                '최대 파일 용량인 100MB를 초과한 <br> 파일은 제외되었습니다.',
                'warning'
            )
            return false;
        } else if (fileObject.name.lastIndexOf('.') === -1) {
            swal(
                'Warning!',
                '확장자가 없는 파일은 제외되었습니다.',
                'warning'
            )
            return false;
        } else if (!fileTypes.includes(fileObject.type)) {
            swal(
                'Warning!',
                '첨부가 불가능한 파일은 제외되었습니다.',
                'warning'
            )
            return false;
        } else {
            return true;
        }
    }
    function deleteFile(fileId) {
        document.querySelector(`#file${fileId}`).remove();
        filesArray[fileId].is_delete = true;
    }


    function filledRate(stars, value) {
        stars.forEach(star => {
            const starValue = parseFloat(star.closest('label').querySelector('input').value);
            if (starValue <= value) {
                star.classList.add('filled');
            }
        });
    }

    function initStars(stars) {
        stars.forEach(star => {
            star.classList.remove('filled');
        });
    }
}