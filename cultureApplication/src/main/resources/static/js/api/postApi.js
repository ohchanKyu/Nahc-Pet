async function deletePostListProcess(posts) {
    try{
        const response = await axios({
            method: "post",
            url: `/post/delete/list`,
            params : {
                posts
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
async function deletePostProcess(postId) {
    try{
        const response = await axios({
            method: "delete",
            url: `/post/delete/${postId}`,
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
async function addCommentProcess(postId,content) {
    try{
        const response = await axios({
            method: "post",
            url: `/comment/add/${postId}`,
            data : {
                content
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
async function deleteCommentProcess(commentId) {
    try{
        const response = await axios({
            method: "delete",
            url: `/comment/delete/${commentId}`,
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
async function editCommentProcess(editorId,content) {
    try{
        const response = await axios({
            method: "patch",
            url: `/comment/patch/${editorId}`,
            data : {
                content
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
async function addPostProcess(title,content) {
    try{
        const response = await axios({
            method: "post",
            url: '/post/add',
            data : {
                title,
                content
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
async function editPostProcess(postId,title,content) {
    try{
        const response = await axios({
            method: "patch",
            url: `/post/patch/${postId}`,
            data : {
                title,
                content
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