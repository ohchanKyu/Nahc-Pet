document.querySelector(".all-post-button").addEventListener("click",function(evt){
    location.href = "/public/board/type/all";
});

document.querySelector(".delete-post-button").addEventListener("click",async function(evt){
    const checkButton = document.querySelectorAll("#check-button");
    const delete_list = [];
    for(let i=0;i<checkButton.length;i++){
        const checkValue = checkButton[i].dataset.check;
        if (checkValue === "1"){
            delete_list.push(checkButton[i].dataset.value);
        }
    }
    if (delete_list.length === 0){
        swal(
            'Warning!',
            '삭제하실 항목을 1가지 이상 선택해주세요!',
            'warning'
        )
        return;
    }
    const deleteListResponseData = await deletePostListProcess(delete_list);
    if (deleteListResponseData){
        swal(
            'Success!',
            '삭제 성공하였습니다.',
            'success'
        ).then(result => {
            if (result.value) {
                location.href = `/private/board/me`;
            }
        })
    }
});

const checkButton = document.querySelectorAll("#check-button");

for(let i=0;i<checkButton.length;i++){
    checkButton[i].addEventListener("click",function(evt){
        target_bar = evt.target;
        value = target_bar.dataset.check;
        if (value === "0"){
            target_bar.style.fontWeight="bold";
            target_bar.style.color = "#e45735";
            target_bar.dataset.check = "1";
        }else{
            target_bar.style.color = "#000";
            target_bar.dataset.check = "0";
            target_bar.style.fontWeight="400";
        }
    });
}

const all_check = document.querySelector("#all_check");

all_check.addEventListener("click",function(evt){

    const all_value = evt.target.dataset.value;
    const checkButton = document.querySelectorAll("#check-button");
    if (all_value === "0"){
        evt.target.dataset.value = "1";
        for(let i=0;i<checkButton.length;i++){
            checkButton[i].style.fontWeight =  "bold";
            checkButton[i].style.color =  "#e45735";
            checkButton[i].dataset.check = "1";
        }
    }else{
        evt.target.dataset.value = "0";
        for(let i=0;i<checkButton.length;i++){
            checkButton[i].style.color =  "#000";
            checkButton[i].dataset.check = "0";
            checkButton[i].style.fontWeight =  "400";
        }
    }
});