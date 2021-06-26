function randomDate(start) {
    return new Date(start.getTime() + Math.random() * (start.getTime()));
}

function GetSortOrder(prop) {
    return function (a, b) {
        if (a[prop] > b[prop]) {
            return 1;
        } else if (a[prop] < b[prop]) {
            return -1;
        }
        return 0;
    }
}
const imglist = [{
    "title": "1",
    "category": "A",
    "date": randomDate(new Date())
}, {
    "title": "2",
    "category": "B",
    "date": randomDate(new Date())
}, {
    "title": "3",
    "category": "C",
    "date": randomDate(new Date())
}, {
    "title": "4",
    "category": "B",
    "date": randomDate(new Date())
}, {
    "title": "5",
    "category": "A",
    "date": randomDate(new Date())
}, {
    "title": "6",
    "category": "C",
    "date": randomDate(new Date())
}, {
    "title": "7",
    "category": "B",
    "date": randomDate(new Date())
}, {
    "title": "8",
    "category": "C",
    "date": randomDate(new Date())
}, {
    "title": "9",
    "category": "A",
    "date": randomDate(new Date())
}, {
    "title": "10",
    "category": "C",
    "date": randomDate(new Date())
}, {
    "title": "11",
    "category": "B",
    "date": randomDate(new Date())
}, {
    "title": "12",
    "category": "B",
    "date": randomDate(new Date())
}, {
    "title": "13",
    "category": "C",
    "date": randomDate(new Date())
}, {
    "title": "14",
    "category": "A",
    "date": randomDate(new Date())
}, {
    "title": "15",
    "category": "A",
    "date": randomDate(new Date())
}]

var f_img_list = []

document.addEventListener('DOMContentLoaded', function () {
    sortnfilter();
}, false);

function sortnfilter() {
    var filter_cat = document.getElementById("cat");
    var f_cat = filter_cat.options[filter_cat.selectedIndex].value;
    var sort_e = document.getElementById("sort");
    var sort = sort_e.options[sort_e.selectedIndex].value;
    f_img_list = imglist
    if (f_cat != "none" && sort != "none") {
        f_img_list = imglist.filter(img => {
            return img.category == f_cat;
        });
        f_img_list.sort(GetSortOrder(sort))
    } else if (f_cat != "none") {
        f_img_list = imglist.filter(img => {
            return img.category == f_cat;
        });
    } else if (sort != "none") {
        f_img_list.sort(GetSortOrder(sort))
    }
    console.log(f_img_list)
    render_images();
}

function render_images() {
    var e_container = document.getElementById("grid-container");
    var div_my_model = document.getElementById("myModal");
    if (e_container != null)
        e_container.innerHTML = "";

    // if (div_my_model != null)
    //     div_my_model.innerHTML = "";
  
   
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function() { 
        div_my_model.style.display = "none";
    }
    for (var i = 0; i < f_img_list.length && i < 9; i++) {


        var new_div = document.createElement("div")
        new_div.setAttribute("class", "grid-item")
        var n_img = document.createElement("img")
        var ititle = f_img_list[i].title;
        n_img.setAttribute("src", "images/" + f_img_list[i].title + ".jpg")
        n_img.setAttribute("class", "hover-shadow cursor");
        n_img.onclick = function () {
            var modalImg = document.getElementById("modal_image");
            modalImg.setAttribute("src" , "images/" + ititle + ".jpg"); 
            div_my_model.style.display = "block";
        }
        new_div.appendChild(n_img);
        e_container.appendChild(new_div);
    }
}


