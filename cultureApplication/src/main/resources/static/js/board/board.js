const prevButton = document.querySelector("#prevButton");
const nextButton = document.querySelector("#nextButton");
const prevSection = document.querySelector("#prevSection");
const nextSection = document.querySelector("#nextSection");

if (posts.length === 0){
    prevButton.style.display = "none";
    nextButton.style.display = "none";
    prevSection.style.display = "none";
    nextSection.style.display = "none";
}
if (currentSection === 1){
    prevSection.style.display = "none";
}
if (currentSection === endSection){
    nextSection.style.display = "none";
}
if (currentPage === 1){
    prevButton.style.display = "none";
}
if (currentPage === totalPage){
    nextButton.style.display = "none";
}

prevSection.addEventListener("click",function(evt){
    let pageNumber;
    if (currentSection === 2){
        pageNumber = 1;
    }else{
        pageNumber = (currentSection - 2) * 5 + 1;
    }
    if (postType !== 'me'){
        location.href = `/public/board/type/${postType}?start=${parseInt(pageNumber)}`;
    }else{
        location.href = `/private/board/${postType}?start=${parseInt(pageNumber)}`;
    }
});
nextSection.addEventListener("click",function(evt){
    const pageNumber = 5 * currentSection + 1;
    if (postType !== 'me'){
        location.href = `/public/board/type/${postType}?start=${parseInt(pageNumber)}`;
    }else{
        location.href = `/private/board/${postType}?start=${parseInt(pageNumber)}`;
    }
});
prevButton.addEventListener("click",function(evt){
    if (postType !== 'me'){
        location.href = `/public/board/type/${postType}?start=${parseInt(currentPage-1)}`;
    }else{
        location.href = `/private/board/${postType}?start=${parseInt(currentPage-1)}`;
    }
});
nextButton.addEventListener("click",function(evt){
    if (currentPage !== totalPage){
        if (postType !== 'me'){
            location.href = `/public/board/type/${postType}?start=${parseInt(currentPage+1)}`;
        }else{
            location.href = `/private/board/${postType}?start=${parseInt(currentPage+1)}`;
        }
    }
});
document.querySelector(".add-post-button").addEventListener("click",function(evt){
    location.href = "/private/board/write";
});

if (document.querySelector(".search-box")){
    document.querySelector(".search-box").addEventListener('click',function (evt){
        const inputValue = document.querySelector("#input").value;
        location.href = `/public/board/type/keyword?keyword=${inputValue}`;
    })
}


const post_number = document.querySelectorAll(".text_number");
if (postType === "all"){
    document.querySelector("#all").className = "select-active";
}else if (postType === "date"){
    document.querySelector("#date").className = "select-active";
}else if (postType === "count"){
    document.querySelector("#count").className = "select-active";
}

for(let i=0;i<posts.length;i++){
    post_number[i].innerHTML = (currentPage-1) * 10 + i + 1;
}

let numberLinkBody = document.querySelector(".move_number");
let pageStartNumber = 5 * currentSection - 4;
let pageEndNumber = 5 * currentSection;

for(let i=pageStartNumber;i<=pageEndNumber;i++){
    if (i > totalPage){
        break;
    }
    const numberLinkTag = document.createElement('a');
    numberLinkTag.setAttribute("class","number");
    numberLinkTag.setAttribute("data-value", i);
    if (postType !== 'me'){
        numberLinkTag.setAttribute("href",`/public/board/type/${postType}?start=${i}`);
    }else{
        numberLinkTag.setAttribute("href",`/private/board/${postType}?start=${i}`);
    }
    numberLinkTag.innerHTML = i;
    numberLinkBody.append(numberLinkTag);
}

const pageList = document.querySelectorAll(".number");

for(let i=0;i<pageList.length;i++){
    if (currentPage.toString() === pageList[i].dataset.value){
        pageList[i].className = "number page-active";
    }
}