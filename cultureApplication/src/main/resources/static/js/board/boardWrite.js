document.querySelector("#all").addEventListener("click",function(evt){
    location.href = "/public/board/type/all";
});
document.querySelector("#me").addEventListener("click",function(evt){
    location.href = `/private/board/me`;
});

let myEditor;
ClassicEditor
    .create(document.querySelector("#editor"),{
        removePlugins: ['MediaEmbed'],
        ckfinder: {
            uploadUrl : '/private/board/upload'
        }
    })
    .then(editor => {
        myEditor = editor;
        console.log('Editor was initialized');
    })
    .catch(error => {
        console.log(error);
    });

document.querySelector(".form-submit").addEventListener("click",async function(evt){

    const titleInput = document.querySelector("#title").value;
    const editorInput =  myEditor.getData();

    if (editorInput.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 내용을 입력해주세요.',
            'warning'
        )
        return;
    }
    if (editorInput.trim().length > 10000){
        swal(
            'Warning!',
            '본문 글자수는 최대 10000 글자 입니다!',
            'warning'
        )
        return;
    }
    if (titleInput.trim().length === 0){
        swal(
            'Warning!',
            '입력된 내용이 없습니다 <br/> 제목을 입력해주세요.',
            'warning'
        )
        return;
    }
    if (titleInput.trim().length > 255){
        swal(
            'Warning!',
            '제목 글자수는 최대 255 글자 입니다!',
            'warning'
        )
        return;
    }
    if (type === 'write'){
        const addPostResponseData = await addPostProcess(titleInput,editorInput);
        if (addPostResponseData){
            swal(
                'Success!',
                '등록이 완료되었습니다!',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/public/board/type/all";
                }
            })
        }

    }else{
        const editPostResponseData = await editPostProcess(postId,titleInput,editorInput);
        if (editPostResponseData){
            swal(
                'Success!',
                '수정이 완료되었습니다!',
                'success'
            ).then(result => {
                if (result.value) {
                    location.href = "/public/board/type/all";
                }
            })
        }
    }
});