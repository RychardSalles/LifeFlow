function logout() {
    sessionStorage.clear();
    // Retorna para a raiz (index.html) saindo da pasta /paginas
    window.location.href = '../index.html';
}

function openNav() {
    document.getElementById("mySidenav").style.width = "15%";
    document.querySelector(".btn-menu").src = "../img/close.png";
}
function closeNav() { 
    document.getElementById("mySidenav").style.width = "0%";
    document.querySelector(".btn-menu").src = "../img/menu-bar.png";
}

function toggleNav() {
    const sidenav = document.getElementById("mySidenav");
    if (sidenav.style.width > "0%") { 
        closeNav();
    } else {
        openNav();
    }
}

document.addEventListener("click", function(event) { 
    const sidenav = document.getElementById("mySidenav");
    const menuButtonImg = document.querySelector(".btn-menu"); 

    if (sidenav.style.width > "0%" && 
        !sidenav.contains(event.target) && 
        !(menuButtonImg.parentElement && menuButtonImg.parentElement.contains(event.target))) { 
        closeNav();
    }
});

function displayUserInfo() {
    const userArea = document.getElementById('user-profile-area');
    const storedName = sessionStorage.getItem('user_name');
    
    if (userArea && storedName) {
        userArea.innerHTML = `
            <div class="user-profile-card" onclick="this.classList.toggle('active'); event.stopPropagation();">
                <span id="user-info-name">👤 ${storedName}</span>
                <ul class="user-options-list">
                    <li onclick="logout()">Sair da Conta</li>
                </ul>
            </div>
        `;
    }
}

async function loadDashboardChart() {
    const canvas = document.getElementById('myPieChart');
    if (!canvas) 
        return;

    const ctx = canvas.getContext('2d');

    try {
        const response = await fetchAutenticado('http://localhost:8080/api/dashboard/chart-data'); 
        
        if (!response.ok) 
            throw new Error('Erro ao carregar dados da API');

        const data = await response.json();

        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: data.labels,
                datasets: [{
                    data: data.values,
                    backgroundColor: [
                        '#5ce1e6',
                        '#3195f2',
                        '#bafffb',
                        '#84bfae',
                        '#d9d9d9'  
                    ],
                    borderWidth: 2,
                    borderColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    } catch (error) {
        console.error('Erro ao inicializar gráfico:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    displayUserInfo();
    loadDashboardChart();
});