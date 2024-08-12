async function addReviewProcess(formData,currentPlaceId){
    try{
        const response = await axios({
            method: "post",
            url: `/review/add/${currentPlaceId}`,
            data: formData,
            headers: {
                'Content-Type': 'multipart/form-data'
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
async function deleteReviewProcess(reviewId){
    try{
        const response = await axios({
            method: "delete",
            url: `/review/delete/${reviewId}`,
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
async function getReviewFileProcess(reviewId){
    try{
        const response = await axios({
            method: "get",
            url: `/review/file/${reviewId}`,
            responseType: 'json',
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
async function checkIsAlreadyProcess(placeId){
    try{
        const response = await axios({
            method: "get",
            url: `/review/check/${placeId}`,
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
async function getOneReviewProcess(reviewId){
    try{
        const response = await axios({
            method: "get",
            url: `/review/${reviewId}`,
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
async function getAllReviewByPlaceIdProcess(placeId){
    try{
        const response = await axios({
            method: "get",
            url: `/review/getAll/${placeId}`,
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
async function editOneReviewProcess(formData,reviewId){
    try{
        const response = await axios({
            method: "post",
            url: `/review/edit/${reviewId}`,
            data: formData,
            headers: {
                'Content-Type': 'multipart/form-data'
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