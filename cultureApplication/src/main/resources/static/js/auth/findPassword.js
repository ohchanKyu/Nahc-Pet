let authEmail;

const hideAndShowWrapper = (type, isHide) => {
    const targetBox = document.getElementById(type);
    if (isHide){
        targetBox.style.display = "none";
    }else{
        targetBox.classList.add("animation-tag");
        targetBox.style.display = "block";
    }
};
hideAndShowWrapper("verify-wrapper",true);
hideAndShowWrapper("change-password-wrapper",true);

const sendMail = async (email) => {

    const sendMailResponseData= await sendMailProcess(email);
    if (sendMailResponseData){
        swal(
            'Success!',
            '전송 완료하였습니다. <br/> 인증번호를 확인해주세요!',
            'success'
        )
        authEmail = email;
        hideAndShowWrapper("verify-wrapper",false);
        hideAndShowWrapper("email-wrapper",true);
    }else{
        swal(
            'Error!',
            '전송에 실패하였습니다. <br/> 다시 시도해주세요!',
            'error'
        )
        hideAndShowWrapper("verify-wrapper",true);
        hideAndShowWrapper("email-wrapper",false);
    }
};
document.querySelector("#change-password-button").addEventListener("click",async function (evt) {
    evt.target.disabled = true;
    evt.target.innerText = "변경중...";

    const passwordInput = document.querySelector("#password").value;
    const passwordCheckInput = document.querySelector("#passwordCheck").value;

    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;

     if(!passwordRegex.test(passwordInput)){
        swal(
            'Warning!',
            '영문자,숫자,특수문자를 하나 포함, <br/> (8~15)자리로 만들어주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "비밀번호 변경";
        return;
    }else if (passwordInput !== passwordCheckInput){
        swal(
            'Warning!',
            '비밀번호가 일치하지 않습니다. <br/> 다시 한 번 확인해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "비밀번호 변경";
        return;
    }
    const editPasswordResponseData= await editPasswordProcess(authEmail,passwordInput);
    if (editPasswordResponseData){
        swal(
            'Success!',
            '비밀번호 변경에 성공하였습니다. <br/> 로그인 페이지로 이동합니다.',
            'success'
        ).then(result => {
            location.href = "/auth/login";
        })
    }else{
        swal(
            'Error!',
            '비밀번호 변경에 실패하였습니다. <br/> 다시 시도해주세요!',
            'error'
        )
        evt.target.disabled = false;
        evt.target.innerText = "비밀번호 변경";
    }

});

document.querySelector("#verify-code-button").addEventListener("click",async function (evt) {
    evt.target.disabled = true;
    evt.target.innerText = "확인중...";

    const verifyCode = document.querySelector("#code").value;
    if (verifyCode.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 인증번호를 입력해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "인증번호 확인";
        return;
    }
    const verifyCodeResponseData = await verifyCodeProcess(authEmail, verifyCode.trim());
    if (verifyCodeResponseData){
        hideAndShowWrapper("change-password-wrapper",false);
        hideAndShowWrapper("verify-wrapper",true);
        hideAndShowWrapper("email-wrapper",true);
    }else{
        swal(
            'Error!',
            '인증번호가 일치하지 않습니다. <br/> 다시 시도해주세요!',
            'error'
        )
        evt.target.disabled = false;
        evt.target.innerText = "인증번호 확인";
    }
});

document.querySelector("#find-password-button").addEventListener("click",async function (evt) {
    evt.target.disabled = true;
    evt.target.innerText = "전송중...";

    const emailInput = document.querySelector("#email").value;
    if (emailInput.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 이메일을 입력해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "인증번호 전송";
        return;
    }
    const isExistResponseData = await isExistEmailProcess(emailInput);
    if (isExistResponseData){
        await sendMail(emailInput);
    }else{
        swal(
            'Error!',
            '가입된 이메일 정보가 없습니다.<br/> 다시 시도해주세요!',
            'error'
        )
        evt.target.disabled = false;
        evt.target.innerText = "인증번호 전송";
    }
});