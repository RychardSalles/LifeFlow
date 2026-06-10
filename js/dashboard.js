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
        const usuarioId = sessionStorage.getItem('user_id');
        const response = await fetchAutenticado(`http://localhost:8080/api/tarefas?usuarioId=${usuarioId}`); 
        
        if (!response.ok) 
            throw new Error('Erro ao carregar dados da API');

        const tarefas = await response.json();
        
        // Contabiliza tarefas por status
        const concluidas = tarefas.filter(t => t.statusTarefa === "Concluída").length;
        const pendentes = tarefas.length - concluidas;

        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ["Concluídas", "Pendentes"],
                datasets: [{
                    data: [concluidas, pendentes],
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

function initCalendar() {
    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return;

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'pt-br',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek'
        },
        selectable: true,
        businessHours: true,
        editable: true,
        select: async function(info) {
            const tipo = prompt('Digite "T" para nova Tarefa ou "E" para novo Evento:').toUpperCase();
            
            if (tipo !== 'T' && tipo !== 'E') {
                calendar.unselect();
                return;
            }

            const title = prompt('Digite o título:');
            if (title) {
                 const descricao = prompt('Digite uma breve descrição:');
                const usuarioId = sessionStorage.getItem('user_id') || "6";

                let url = "";
                let corpo = {};

                if (tipo === 'T') {
                    url = 'http://localhost:8080/api/tarefas';
                    corpo = {
                        titulo: title,
                        descricao: descricao,
                        dataTarefa: info.startStr,
                        statusTarefa: "Pendente",
                        usuarioId: usuarioId.toString()
                    };
                } else {
                    url = 'http://localhost:8080/api/eventos';
                    // Ajusta formato para LocalDateTime (YYYY-MM-DDTHH:mm:ss)
                    const dataEvento = info.startStr.includes("T") ? info.startStr : info.startStr + "T00:00:00";
                    corpo = {
                        titulo: title,
                        descricao: descricao,
                        dataEvento: dataEvento,
                        usuarioId: usuarioId.toString()
                    };
                }

                try {
                    const response = await fetch(url, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(corpo)
                    });

                    if (response.ok) {
                        calendar.addEvent({
                            title: (tipo === 'T' ? "[T] " : "[E] ") + title,
                            start: info.startStr,
                            backgroundColor: tipo === 'T' ? '#5ce1e6' : '#3195f2',
                            allDay: info.allDay
                        });
                    } else {
                        alert('Erro ao salvar no servidor.');
                    }
                } catch (error) {
                    console.error("Erro na requisição:", error);
                }
            }
            calendar.unselect();
        },
        events: async function(info, successCallback, failureCallback) {
            try {
                const usuarioId = sessionStorage.getItem('user_id');
                
                const [resTarefas, resEventos] = await Promise.all([
                    fetchAutenticado(`http://localhost:8080/api/tarefas?usuarioId=${usuarioId}`),
                    fetchAutenticado(`http://localhost:8080/api/eventos?usuarioId=${usuarioId}`)
                ]);

                const tarefas = await resTarefas.json();
                const eventos = await resEventos.json();

                const formatTarefas = tarefas.map(t => ({
                    title: "[T] " + t.titulo,
                    start: t.dataTarefa,
                    backgroundColor: '#5ce1e6'
                }));

                const formatEventos = eventos.map(e => ({
                    title: "[E] " + e.titulo,
                    start: e.dataEvento,
                    backgroundColor: '#3195f2'
                }));

                successCallback([...formatTarefas, ...formatEventos]);
            } catch (error) {
                console.error("Erro ao carregar eventos do calendário:", error);
                failureCallback(error);
            }
        }
    });

    calendar.render();
}

document.addEventListener('DOMContentLoaded', () => {
    displayUserInfo();
    loadDashboardChart();
    initCalendar();
});