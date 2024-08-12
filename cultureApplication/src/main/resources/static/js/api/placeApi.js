async function getRecommendPlaceProcess(coordinateRequest){
    try{
        const response = await axios({
            method: "post",
            url: `/recommend/place`,
            data: {
                latitude: coordinateRequest.latitude,
                longitude: coordinateRequest.longitude,
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
async function getAroundPlaceProcess(coordinateRequest,query){
    try{
        const response = await axios({
            method: "post",
            url: `/public/pet/around/${query}`,
            data: {
                latitude: coordinateRequest.latitude,
                longitude: coordinateRequest.longitude,
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
async function addressToCoordinateProcess(address){
    try{
        const response = await axios({
            method: "get",
            url: `/public/location/${address}`,
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
async function searchByKeywordProcess(data) {
    try{
        const response = await axios({
            method: "post",
            url: `/public/pet/keyword`,
            data : {
                ...data
            }
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
async function getTrafficInformationProcess(location) {
    try{
        const response = await axios({
            method: "post",
            url: `/public/location/traffic`,
            data : {
                ...location
            }
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
async function getAroundPetCafeProcess(coordinateRequest){
    try{
        const response = await axios({
            method: "post",
            url: `/public/location/around/cafe`,
            data: {
                latitude: coordinateRequest.latitude,
                longitude: coordinateRequest.longitude,
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
async function getPlaceByIdProcess(placeId){
    try{
        const response = await axios({
            method: "get",
            url: `/place/${placeId}`,
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
async function getAroundPetEducationProcess(coordinateRequest){
    try{
        const response = await axios({
            method: "post",
            url: `/public/location/around/education`,
            data: {
                latitude: coordinateRequest.latitude,
                longitude: coordinateRequest.longitude,
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