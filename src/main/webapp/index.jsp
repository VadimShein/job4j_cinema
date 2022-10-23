<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Cinema</title>
</head>
<body onload="updateTickets()">
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<div class="container">
    <div class="row pt-3">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <h3>Выберите место на сеанс</h3>
            </div>
            <div class="card-body">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th style="width: 120px;">Ряд / Место</th>
                            <th>1</th>
                            <th>2</th>
                            <th>3</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr id="tr1">
                            <th>1</th>
                            <td><input type="radio" name="place" value="11"> Ряд 1, Место 1</td>
                            <td><input type="radio" name="place" value="12"> Ряд 1, Место 2</td>
                            <td><input type="radio" name="place" value="13"> Ряд 1, Место 3</td>
                        </tr>
                        <tr id="tr2">
                            <th>2</th>
                            <td><input type="radio" name="place" value="21"> Ряд 2, Место 1</td>
                            <td><input type="radio" name="place" value="22"> Ряд 2, Место 2</td>
                            <td><input type="radio" name="place" value="23"> Ряд 2, Место 3</td>
                        </tr>
                        <tr id="tr3">
                            <th>3</th>
                            <td><input type="radio" name="place" value="31"> Ряд 3, Место 1</td>
                            <td><input type="radio" name="place" value="32"> Ряд 3, Место 2</td>
                            <td><input type="radio" name="place" value="33"> Ряд 3, Место 3</td>
                        </tr>
                    </tbody>
                </table>
                <div class="row pt-3">
                    <button type="button" class="btn btn-success" onclick="getTicket()">Оплатить</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    setTimeout(updateTickets, 2000)
    function updateTickets() {
        $.ajax({
            type: 'GET',
            crossDomain : true,
            url: 'http://localhost:8080/job4j_cinema/hall',
            dataType: 'text',
        }).done(function(data) {
            let dt = JSON.parse(data)
            for (const [key, value] of Object.entries(dt)) {
                let parent = document.querySelector('tbody')
                let fRow = parent.querySelector('#tr' + key)
                for (let i = 0; i < value.length; i++) {
                    let selector = "[value='" + value[i].row + value[i].cell + "']"
                    let td = fRow.querySelector(selector)
                    if (td != null) {
                        td.parentNode.removeChild(td)
                    }
                }
        }
        }).fail(function(err) {
            alert(err);
        });
    }

    function getTicket() {
        let value = document.querySelector("input[name=place]:checked").value
        let row = value[0]
        let cell = value[1]
         window.location.href = "http://localhost:8080/job4j_cinema/payment.jsp?row=" + row + "&cell=" + cell
    }
</script>
</body>
</html>