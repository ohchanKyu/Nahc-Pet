async function AIQuestionProcess(question) {
    try{
        const response = await axios({
            method: "post",
            url: `/api/gemini/question`,
            params : {
                question
            }
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '일시적 오류입니다. <br> 다시 시도해주세요.',
            'error'
        );
    }
}