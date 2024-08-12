const elements = document.querySelectorAll('.in_view');
const otherElements = document.querySelectorAll('.in_sectionView');

function isInViewport(el) {
    const rect = el.getBoundingClientRect();
    const top = Math.max(rect.top, 0);
    const left = Math.max(rect.left, 0);

    return (
        top <= (window.innerHeight || document.documentElement.clientHeight) &&
        left <= (window.innerWidth || document.documentElement.clientWidth)
    );
}
function checkInView() {
    elements.forEach(function(element) {
        if (isInViewport(element)) {
            element.classList.add('visible');
        } else {
            element.classList.remove('visible');
        }
    });
    otherElements.forEach(function(element) {
        if (isInViewport(element)) {
            element.classList.add('secondVisible');
        } else {
            element.classList.remove('secondVisible');
        }
    });
}

window.addEventListener('scroll', checkInView);
window.addEventListener('resize', checkInView);

checkInView();

document.getElementById("first-button").addEventListener("click",function (evt){
    location.href="/public/petInformation/cafe";
});
document.getElementById("second-button").addEventListener("click",function (evt){
    location.href="/public/petInformation/education";
});
document.getElementById("third-button").addEventListener("click",function (evt){
    location.href="/public/petInformation/shelter";
});
document.getElementById("post").addEventListener("click",function (evt){
    location.href="/public/board/type/all";
});
document.getElementById("info").addEventListener("click",function (evt){
    location.href="/private/me/favorite";
});