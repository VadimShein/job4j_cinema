<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Payment</title>
</head>
<body onload="getTicket()">
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

<div class="container">
    <div class="row pt-3">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <div class="msg">
                </div>
            </div>
            <div class="card-body">
                <form action="http://localhost:8080/job4j_cinema/hall" method="post">
                    <div class="form-group">
                        <label for="username">ФИО</label>
                        <input type="text" class="form-control" name="username" id="username" placeholder="ФИО">
                    </div>
                    <div class="form-group">
                        <label for="email">Почтовый адрес</label>
                        <input type="text" class="form-control" name="email" id="email" placeholder="Почтовый адрес">
                    </div>
                    <div class="form-group">
                        <label for="phone">Номер телефона</label>
                        <input type="text" class="form-control" name="phone" id="phone" placeholder="Номер телефона">
                    </div>
                    <div class="form-group">
                        <input type="hidden" class="form-control" name="row" id="row" placeholder="Ряд">
                    </div>
                    <div class="form-group">
                        <input type="hidden" class="form-control" name="cell" id="cell" placeholder="Место">
                    </div>
                    <button type="submit" class="btn btn-success">Оплатить</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function getTicket() {
        let items = window.location.search.substr(1).split("&")
        let row = items[0].split("=")[1]
        let cell = items[1].split("=")[1]
        document.querySelector("input[id=row]").value = row;
        document.querySelector("input[id=cell]").value = cell;

        let message = document.querySelector('.msg')
        let ticketInfo = document.createElement('h3')
        ticketInfo.innerHTML = `Вы выбрали ${row} ряд, ${cell} место. Сумма: 500 рублей`
        message.appendChild(ticketInfo)
    }
</script>
</body>
</html>