<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | New Match</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="${pageContext.request.contextPath}/images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <div>
            <h1>Start new match</h1>
            <div class="new-match-image"></div>
            <div class="form-container center">
                <form method="post" action="${pageContext.request.contextPath}/new-match" id="newMatchForm">
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                    <label class="label-player" for="playerOne">Player one</label>
                    <input class="input-player" id="playerOne" name="player1Name" placeholder="Name" type="text" required title="Enter a name">
                    <label class="label-player" for="playerTwo">Player two</label>
                    <input class="input-player" id="playerTwo" name="player2Name" placeholder="Name" type="text" required title="Enter a name">
                    <input class="form-button" type="submit" value="Start">
                </form>
            </div>
        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a> roadmap.</p>
    </div>
</footer>

<script>
document.getElementById('newMatchForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const player1Name = document.getElementById('playerOne').value;
    const player2Name = document.getElementById('playerTwo').value;
    
    fetch('${pageContext.request.contextPath}/new-match', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            playerName1: player1Name,
            playerName2: player2Name
        })
    })
    .then(response => response.json())
    .then(data => {
        window.location.href = '${pageContext.request.contextPath}/match-score?uuid=' + data.id;
    })
    .catch(error => {
        console.error('Error:', error);
        document.querySelector('.form-container p').textContent = 'Error creating match. Please try again.';
        document.querySelector('.form-container p').style.display = 'block';
    });
});
</script>
</body>
</html> 