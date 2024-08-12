const deleteReviewHandler = async (reviewId) => {
    const deleteReviewResponseData = await deleteReviewProcess(reviewId);
    if (deleteReviewResponseData){
        swal(
            'Success!',
            '리뷰 삭제가 완료되었습니다!',
            'success'
        ).then(result => {
            if (result.value) {
                location.href = `/private/me/post`;
            }
        })
    }
};
const initialReviewDetail = async (reviewId) => {

    const getAllFileList = await getReviewFileProcess(reviewId);
    const reviewData = await getOneReviewProcess(reviewId);
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
    fetchReviewDetailModal(reviewData,imageList);
};
const fetchReviewDetailModal = (reviewData,imageData) => {
    document.querySelector("#review-detail-modal .modal-title").innerHTML = `# ${reviewData.place.placeName}`;
    const textBox = document.querySelector("#review-detail-modal .text-box");
    const fileBox = document.querySelector("#review-detail-modal .file-list-box");
    while (textBox.firstChild) {
        textBox.removeChild(textBox.firstChild);
    }
    while (fileBox.firstChild) {
        fileBox.removeChild(fileBox.firstChild);
    }
    textBox.innerHTML = `
        <div class="review-desc">
            <p>
                <i class="fa-solid fa-star" style="color:#ffdd11;"></i> 
                    <span style="color:black">${reviewData.rating.toFixed(1)}</span> / 5.0 
            </p>
            <p> · 작성일 ${reviewData.createdDateTime.split("T")[0]}</p>
            <p> · 사진 ${imageData.length}개 등록</p>
        </div>
        <p class="review-text">
            ${reviewData.text}
        </p>
    `
    for(let i=0;i<imageData.length;i++){
        let imageBox = document.createElement('div');
        imageBox.innerHTML = `
            <img src="${imageData[i]}" class="review-image" alt="review-image">
        `
        imageBox.querySelector('img').addEventListener('click', () => openImageModal(imageData[i],reviewData,imageData));
        fileBox.append(imageBox);
    }
};
const openImageModal = (src,reviewData,imageData) => {
    const modal = document.getElementById("image-modal");
    const fullImage = document.getElementById("full-image");
    const subText = document.querySelector('.sub-text');
    fullImage.src = src;
    subText.innerHTML = `
       <div class="sub-desc">
            <p>
                <i class="fa-solid fa-star" style="color:#ffdd11;"></i> 
                    <span>${reviewData.rating.toFixed(1)}</span> / 5.0 
            </p>
            <p> · 작성자 ${reviewData.member.name}</p>
            <p> · 작성일  ${reviewData.createdDateTime.split("T")[0]}</p>
            <p> · 사진 ${imageData.length}개 등록</p>
        </div>
        <p class="sub-desc-text">
            ${reviewData.text}
        </p>
    `
    modal.style.display = "block";
};
if (document.querySelector(".close")){
    document.querySelector(".close").addEventListener("click", () => {
        document.getElementById("image-modal").style.display = "none";
    });
}
window.addEventListener("click", (event) => {
    const modal = document.getElementById("image-modal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
});