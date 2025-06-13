<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Matches</title>
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
        <div class="matches-container">
            <h1>Matches</h1>
            <div class="matches-list">
                <c:forEach items="${matches}" var="match">
                    <div class="match-card">
                        <div class="match-header">
                            <h3>${match.player1Name} vs ${match.player2Name}</h3>
                            <span class="match-status ${match.finished ? 'finished' : 'ongoing'}">
                                ${match.finished ? 'Finished' : 'Ongoing'}
                            </span>
                        </div>
                        <div class="match-score">
                            <div class="player-score">
                                <span class="player-name">${match.player1Name}</span>
                                <span class="score">${match.countSetsPlayer1} - ${match.countGamesPlayer1} - ${match.scorePlayer1}</span>
                            </div>
                            <div class="player-score">
                                <span class="player-name">${match.player2Name}</span>
                                <span class="score">${match.countSetsPlayer2} - ${match.countGamesPlayer2} - ${match.scorePlayer2}</span>
                            </div>
                        </div>
                        <c:if test="${!match.finished}">
                            <a href="${pageContext.request.contextPath}/match-score?uuid=${match.id}" class="button">Continue Match</a>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a> roadmap.</p>
    </div>
</footer>
</body>
</html> 