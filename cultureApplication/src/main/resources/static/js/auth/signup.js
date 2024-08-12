document.querySelector("#signup-button").addEventListener("click",async function(evt){

    evt.target.disabled = true;
    evt.target.innerText = "제출중...";

    const nameInput = document.querySelector("#name").value;
    const emailInput = document.querySelector("#email").value;
    const passwordInput = document.querySelector("#password").value;
    const passwordCheckInput = document.querySelector("#passwordCheck").value;

    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (nameInput.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 이름을 입력해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "회원가입";
        return;
    }else if (emailInput.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 이메일을 입력해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "회원가입";
        return;
    }else if (!emailRegex.test(emailInput)) {
        swal(
            'Warning!',
            '잘못된 이메일 형식입니다.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "회원가입";
        return;
    }else if (!passwordRegex.test(passwordInput)){
        swal(
            'Warning!',
            '영문자,숫자,특수문자를 하나 포함, <br/> (8~15)자리로 만들어주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "회원가입";
        return;
    }else if (passwordInput !== passwordCheckInput){
        swal(
            'Warning!',
            '비밀번호가 일치하지 않습니다. <br/> 다시 한 번 확인해주세요.',
            'warning'
        )
        evt.target.disabled = false;
        evt.target.innerText = "회원가입";
        return;
    }
    const signupResponseData = await signupNewMemberProcess({
        name : nameInput,
        email : emailInput,
        password: passwordInput,
    },evt);
    if (signupResponseData){
        evt.target.disabled = false;
        evt.target.innerText = "회원가입";
        swal(
            'Success!',
            '회원가입에 성공하였습니다. <br/> 로그인 페이지로 이동합니다.',
            'success'
        ).then(result => {
            if (result.value) {
                location.href = "/auth/login";
            }
        })
    }
});