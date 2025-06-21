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
            
            <!-- Форма поиска -->
            <div class="search-container">
                <form class="search-form" method="GET" action="${pageContext.request.contextPath}/matches">
                    <div class="search-input-group">
                        <input type="text" 
                               name="filter_by_player_name" 
                               value="${playerName}" 
                               placeholder="Поиск по имени игрока..." 
                               class="search-input">
                        <button type="submit" class="search-btn">
                        </button>
                    </div>
                    <c:if test="${not empty playerName}">
                        <a href="${pageContext.request.contextPath}/matches" class="clear-search">Очистить поиск</a>
                    </c:if>
                </form>
            </div>
            
            <div class="matches-list">
                <c:forEach items="${matches}" var="match">
                    <div class="match-card">
                        <div class="match-header">
                            <h3>${match.player1.name} vs ${match.player2.name}</h3>
                            <span class="match-status finished">Finished</span>
                        </div>
                        <div class="match-content">
                            <div class="player-result">
                                <span class="player-name">${match.player1.name}</span>
                                <span class="winner-indicator ${match.winner.name eq match.player1.name ? 'winner' : ''}">
                                    ${match.winner.name eq match.player1.name ? 'Winner' : ''}
                                </span>
                            </div>
                            <div class="player-result">
                                <span class="player-name">${match.player2.name}</span>
                                <span class="winner-indicator ${match.winner.name eq match.player2.name ? 'winner' : ''}">
                                    ${match.winner.name eq match.player2.name ? 'Winner' : ''}
                                </span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <!-- Пагинация -->
            <div class="pagination">
                <c:if test="${pageNumber > 1}">
                    <a class="pagination-btn" href="${pageContext.request.contextPath}/matches?page=${pageNumber - 1}&filter_by_player_name=${playerName}">Назад</a>
                </c:if>
                <span>Страница ${pageNumber}</span>
                <c:if test="${hasNextPage}">
                    <a class="pagination-btn" href="${pageContext.request.contextPath}/matches?page=${pageNumber + 1}&filter_by_player_name=${playerName}">Вперёд</a>
                </c:if>
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