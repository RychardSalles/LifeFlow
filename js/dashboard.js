function openNav() {
    document.getElementById("mySidenav").style.width = "15%";
    document.querySelector(".btn-menu").src = "../img/close.png";
}
function closeNav() { 
    document.getElementById("mySidenav").style.width = "0%";
    document.querySelector(".btn-menu").src = "../img/menu-bar.png";
}

// Abrir e Fechar o Menu
function toggleNav() {
    const sidenav = document.getElementById("mySidenav");
    if (sidenav.style.width > "0%") { 
        closeNav();
    } else {
        openNav();
    }
}

// Listener de evento para cliques fora do sidenav
document.addEventListener("click", function(event) { 
    const sidenav = document.getElementById("mySidenav");
    const menuButtonImg = document.querySelector(".btn-menu"); 

    if (sidenav.style.width > "0%" && 
        !sidenav.contains(event.target) && 
        !(menuButtonImg.parentElement && menuButtonImg.parentElement.contains(event.target))) { 
        closeNav();
    }
});

// Função para buscar dados da API e renderizar o gráfico
async function loadDashboardChart() {
    const canvas = document.getElementById('myPieChart');
    if (!canvas) 
        return;

    const ctx = canvas.getContext('2d');

    try {
        const response = await fetch('http://localhost:8080/api/dashboard/chart-data'); // Denovo verifique o localhost certo se mudar no seu
        
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

document.addEventListener('DOMContentLoaded', loadDashboardChart);