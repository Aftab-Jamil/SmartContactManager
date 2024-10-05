console.log('success milega');
const menu = document.querySelector(".menu");
const cancel = document.querySelector(".cancel");
const content = document.querySelector(".content");
const sidebar = document.querySelector(".sidebar");
console.log(content);
menu.addEventListener("click", () => {
    sidebar.style.left = "0px";
    content.style.marginTop = "10px";
	content.style.marginLeft="320px";
});

cancel.addEventListener("click", () => {
    sidebar.style.left = "-320px"; // move the sidebar out of view
	content.style.marginTop = "10px";
	content.style.marginLeft = "10px";
});
const search=document.querySelector("#search-box");
const result=document.querySelector(".search-result");
const search_result=async()=>{
    let query=search.value
    if(query==""){
        result.style.display="none";

    }else{
        let url=`http://localhost:8080/search/${query}`;
    console.log(url)
    let data=await fetch(url);
    let parsedata=await data.json();
    console.log(parsedata)
    console.log(Array.isArray(parsedata))
    let resultItem=`<div class="list-group">`
    parsedata.forEach((element) => {
        resultItem+=`<a type="button" class="list-group-item list-group-item-action" href="/user/${element.cId}/contact">${element.name+" "+element.secondName}
  </a>`        
    });
    resultItem+=`</div>`
    result.innerHTML=resultItem;
    result.style.display="block";

    }
    
}