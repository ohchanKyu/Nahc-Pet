let myEditor;
const editCommentArray = [];

ClassicEditor
    .create(document.querySelector("#editor"),{
        removePlugins: ['Image', 'ImageCaption', 'ImageStyle', 'ImageToolbar', 'ImageUpload', 'MediaEmbed'],
    })
    .then(editor => {
        myEditor = editor;
        console.log('Editor was initialized');
    })
    .catch(error => {
        console.log(error);
    });
if (document.querySelector("#admin-delete-button")){
    document.querySelector("#admin-delete-button").addEventListener("click",async function(evt){

        const deletePostResponseData = await deletePostProcess(postId);
        if (deletePostResponseData){
            swal(
                'Success!',
                '삭제가 완료되었습니다.',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/public/board/type/all";
                }
            })
        }
    });
}
if (document.querySelector(".delete-button")){
    document.querySelector(".delete-button").addEventListener("click",async function(evt){

        const deletePostResponseData = await deletePostProcess(postId);
        if (deletePostResponseData){
            swal(
                'Success!',
                '삭제가 완료되었습니다.',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/public/board/type/all";
                }
            })
        }
    });
}
if (document.querySelector(".comment-button")){
    document.querySelector(".comment-button").addEventListener("click",async function (evt){
        evt.preventDefault();
        const editorInput =  myEditor.getData();
        if (editorInput.trim().length === 0){
            swal(
                'Warning!',
                '입력된 내용이 없습니다 <br/> 내용을 입력해주세요.',
                'warning'
            )
            return;
        }
        if (editorInput.trim().length > 1000){
            swal(
                'Warning!',
                '댓글 글자수는 최대 1000 글자 입니다!',
                'warning'
            )
            return;
        }
        const addCommentResponseData = await addCommentProcess(postId,content);
        if (addCommentResponseData){
            swal(
                'Success!',
                '등록이 완료되었습니다!',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = `/public/board/${postId}`;
                }
            })
        }
    })
}
if (document.querySelector(".reset-button")){
    document.querySelector(".reset-button").addEventListener("click",function (evt){
        myEditor.setData("");
    })
}

const initializeEditor = (element,commentId) => {
    const editorContainer = document.querySelector(`#editor${commentId}`);
    const isCreate = editorContainer.dataset.value;
    if (isCreate !== "1") {
        ClassicEditor
            .create(editorContainer, {
                removePlugins: ['Image', 'ImageCaption', 'ImageStyle', 'ImageToolbar', 'ImageUpload', 'MediaEmbed'],
            })
            .then(editor => {
                editCommentArray.push({
                    id : commentId,
                    editor : editor,
                });
                const targetEditor = editCommentArray.find((item) => {
                    return commentId === item.id;
                })
                targetEditor.editor.setData(element.getAttribute("data"));
                console.log('Editor was initialized');
            })
            .catch(error => {
                console.error('에디터 생성 중 오류 발생:', error);
            });
        editorContainer.dataset.value = "1";
    }else{
        const targetEditor = editCommentArray.find((item) => {
            return commentId === item.id;
        })
        targetEditor.editor.setData(element.getAttribute("data"));
    }
};

const deleteComment  = async (commentId) => {
    const deleteCommentResponseData = await deleteCommentProcess(commentId);
    if (deleteCommentResponseData){
        swal(
            'Success!',
            '삭제가 완료되었습니다!',
            'success'
        ).then(result => {
            if (result.value) {
                location.href = `/public/board/${postId}`;
            }
        })
    }
};
const resetEditEditor = (editorId) => {
    const targetEditor = editCommentArray.find((item) => {
        return editorId === item.id;
    })
    targetEditor.editor.setData("");
};
const editCommentSubmit = async (editorId) => {
    const targetEditor = editCommentArray.find((item) => {
        return editorId === item.id;
    });
    const contentValue = targetEditor.editor.getData();
    if (contentValue.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 내용을 입력해주세요.',
            'warning'
        )
        return;
    }
    if (contentValue.trim().length > 1000){
        swal(
            'Warning!',
            '댓글 글자수는 최대 1000 글자 입니다!',
            'warning'
        )
        return;
    }
    const editCommentResponseData = await editCommentProcess(editorId,contentValue);
    if (editCommentResponseData){
        swal(
            'Success!',
            '수정이 완료되었습니다!',
            'success'
        ).then(result => {
            if (result.value) {
                location.href = `/public/board/${postId}`;
            }
        })
    }
};