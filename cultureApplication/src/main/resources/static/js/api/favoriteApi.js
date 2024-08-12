async function addFavoriteProcess(placeId) {
    try{
        const response = await axios({
            method: "post",
            url: `/favorite/add/${placeId}`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '로그인이 필요합니다. <br/> 로그인 후 시도해주세요!',
            'error'
        );
    }
}
async function deleteFavoriteProcess(favoriteId) {
    try{
        const response = await axios({
            method: "delete",
            url: `/favorite/delete/${favoriteId}`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '로그인이 필요합니다. <br/> 로그인 후 시도해주세요!',
            'error'
        );
    }
}
async function isFavoriteProcess(placeId){
    try{
        const response = await axios({
            method: "get",
            url: `/favorite/is-favorite/${placeId}`,
        });
        if (response.status === 200){
            return await response.data;
        }
    }catch(error){
        swal(
            'Error!',
            '로그인이 필요합니다. <br/> 로그인 후 시도해주세요!',
            'error'
        );
    }
}