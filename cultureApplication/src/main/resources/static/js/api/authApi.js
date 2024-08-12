async function findEmailProcess(userName) {
    try{
        const response = await axios({
            method: "get",
            url: `/auth/${userName}/getEmail`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function sendMailProcess(email) {
    try{
        const response = await axios({
            method: "post",
            url: `/auth/${email}/verify-code`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function editPasswordProcess(email,newPassword) {
    try{
        const response = await axios({
            method: "patch",
            url: `/auth/password`,
            data : {
                email,
                newPassword
            }
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function verifyCodeProcess(email,verifyCode) {
    try{
        const response = await axios({
            method: "post",
            url: `/auth/isVerify`,
            data : {
                email,
                verifyCode,
            }
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function isExistEmailProcess(email) {
    try{
        const response = await axios({
            method: "get",
            url: `/auth/${email}/isExist`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function signupNewMemberProcess(data,element) {
    try{
        const response = await axios({
            method: "post",
            url: `/auth/member/signup`,
            data : {
               ...data
            }
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        const status = error.response.data.statusCode;
        if (status === 406){
            swal(
                'Error!',
                '이메일이 중복됩니다. <br/> 다시 시도해주세요!',
                'error'
            )
        }
        element.target.disabled = false;
        element.target.innerText = "회원가입";
    }
}
async function editEmailProcess(email) {
    try{
        const response = await axios({
            method: "post",
            url: `/member/email/${email}`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function editNameProcess(name) {
    try{
        const response = await axios({
            method: "post",
            url: `/member/name/${name}`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}
async function deleteMemberProcess() {
    try{
        const response = await axios({
            method: "delete",
            url: `/member`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br/> 다시 시도해주세요!',
            'error'
        );
    }
}