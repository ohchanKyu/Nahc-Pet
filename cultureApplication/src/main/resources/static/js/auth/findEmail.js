const hideErrorMessage = () => {
    const errorMessageWrapper = document.querySelector(".no-email-message");
    const emailListWrapper =  document.querySelector(".email-list");
    errorMessageWrapper.style.display="none";
    emailListWrapper.style.display="none";
};

const insertIdList = (emailList) => {

    const messageTag = document.querySelector(".no-email-message");
    const listTag = document.querySelector(".email-list");

    if (emailList.length === 0){
        listTag.style.display = "none";
        messageTag.style.display = "block";
        messageTag.classList.add("animation-tag");
        messageTag.classList.remove("fade-out");
        document.querySelector(".no-email-message").innerHTML = "<i class=\"fa-solid fa-circle-xmark\"></i> 해당 이름의 아이디가 존재하지 않습니다.";
    }else{
        listTag.style.display = "block";
        messageTag.style.display = "none";
        listTag.classList.add("animation-tag");
        listTag.classList.remove("fade-out");
        const ulTag = document.querySelector(".email-list");
        let body = "";
        body += "<p class='email-message'><i class=\"fa-solid fa-circle-check\"></i> 회원님의 가입하신 이메일입니다. </p>";
        for(let i=0;i<emailList.length;i++){
            body += `
                <li class="target-email">
                   ${emailList[i]}
                </li>
            `
        }
        ulTag.innerHTML = body;
    }
};

document.querySelector("#find-email-button").addEventListener("click",async function (evt) {
    evt.target.disabled = true;
    evt.target.innerText = "검색중...";
    
    const nameInput = document.querySelector("#name").value;
    if (nameInput.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 이름을 입력해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "이메일 찾기";
        return;
    }
    const findEmailResponseData = await findEmailProcess(nameInput);
    if (findEmailResponseData){
        insertIdList(findEmailResponseData);
        evt.target.disabled = false;
        evt.target.innerText = "이메일 찾기";
    }else{
        evt.target.disabled = false;
        evt.target.innerText = "이메일 찾기";
    }
});



