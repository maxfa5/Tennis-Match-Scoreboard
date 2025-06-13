<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Match Score</title>
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
        <div class="match-score-container">
            <h1>Match Score</h1>
            <div class="score-board">
                <div class="player player-one">
                    <h2>${match.player1Name}</h2>
                    <div class="score">
                        <div class="sets">
                            <c:forEach begin="1" end="${match.countSetsPlayer1}" varStatus="loop">
                                <div class="set won">${loop.index}</div>
                            </c:forEach>
                        </div>
                        <div class="games">${match.countGamesPlayer1}</div>
                        <div class="points">${match.scorePlayer1}</div>
                    </div>
                    <button class="score-button" onclick="incrementScore('${match.id}', 1)">+</button>
                </div>
                <div class="player player-two">
                    <h2>${match.player2Name}</h2>
                    <div class="score">
                        <div class="sets">
                            <c:forEach begin="1" end="${match.countSetsPlayer2}" varStatus="loop">
                                <div class="set won">${loop.index}</div>
                            </c:forEach>
                        </div>
                        <div class="games">${match.countGamesPlayer2}</div>
                        <div class="points">${match.scorePlayer2}</div>
                    </div>
                    <button class="score-button" onclick="incrementScore('${match.id}', 2)">+</button>
                </div>
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
function incrementScore(matchId, player) {
    const data = {
        matchId: matchId,
        addToPLayer1: player === 1 ? 1 : 0,
        addToPLayer2: player === 2 ? 1 : 0
    };

    fetch('${pageContext.request.contextPath}/match-score?uuid=' + matchId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        } else {
            throw new Error('Network response was not ok');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
</script>
</body>
</html> 