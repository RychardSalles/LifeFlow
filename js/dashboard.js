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

let myPieChart = null;
async function loadDashboardChart() {
    const canvas = document.getElementById('myPieChart');
    if (!canvas) return;

    if (myPieChart) myPieChart.destroy();
    const ctx = canvas.getContext('2d');

    try {
        const usuarioId = sessionStorage.getItem('user_id');
        const response = await fetchAutenticado(`http://127.0.0.1:8080/api/tarefas?usuarioId=${usuarioId}`); 
        
        if (!response.ok) 
            throw new Error('Erro ao carregar dados da API');

        const tarefas = await response.json();
        
        // Contabiliza tarefas por status
        const concluidas = tarefas.filter(t => t.statusTarefa === "Concluída" || t.statusTarefa === "Concluida").length;
        const pendentes = tarefas.filter(t => t.statusTarefa === "Pendente" || t.statusTarefa === "").length;
        const emAndamento = tarefas.filter(t => t.statusTarefa === "Em Andamento").length;

        myPieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ["Concluídas", "Pendentes", "Em Andamento"],
                datasets: [{
                    data: [concluidas, pendentes, emAndamento],
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

let currentSelectionInfo = null;
let currentCalendar = null;

function fecharModal() {
    const modal = document.getElementById('modalCadastro');
    if (modal) modal.style.display = 'none';
    if (currentCalendar) currentCalendar.unselect();
    document.getElementById('formCadastro').reset();
}

async function excluirTarefa(id) {
    if (!confirm("Tem certeza que deseja excluir esta tarefa?")) return;

    try {
        const response = await fetchAutenticado(`http://127.0.0.1:8080/api/tarefas?id=${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            loadDashboardChart();
            if (currentCalendar) {
                currentCalendar.refetchEvents();
            }
        } else {
            alert('Erro ao excluir tarefa.');
        }
    } catch (error) {
        console.error('Erro na exclusão:', error);
    }
}

async function mudarStatusTarefa(id, titulo) {
    if (!confirm(`Deseja marcar a tarefa "${titulo}" como Concluída?`)) return;

    try {
        const response = await fetchAutenticado('http://127.0.0.1:8080/api/tarefas', {
            method: 'PUT',
            body: JSON.stringify({
                id: id,
                statusTarefa: 'Concluida'
            })
        });

        if (response.ok) {
            // Recarrega o gráfico e o calendário para refletir as mudanças
            loadDashboardChart();
            if (currentCalendar) currentCalendar.refetchEvents();
        } else {
            alert('Erro ao atualizar tarefa.');
        }
    } catch (error) {
        console.error('Erro na atualização:', error);
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
        select: function(info) {
            currentSelectionInfo = info;
            const modal = document.getElementById('modalCadastro');
            if (modal) modal.style.display = 'flex';
        },
        eventClick: function(info) {
            // Se for uma tarefa (marcada com [T]), permite concluir
            if (info.event.title.startsWith("[T]")) {
                const id = info.event.extendedProps.dbId;
                const titulo = info.event.title.replace("[T] ", "");
                
                const acao = prompt(`Tarefa: ${titulo}\n\nDigite:\n1 - Marcar como Concluída\n2 - Excluir Tarefa\n0 - Cancelar`, "1");
                if (acao === "1") {
                    mudarStatusTarefa(id, titulo);
                } else if (acao === "2") {
                    excluirTarefa(id);
                }
            }
        },
        events: async function(info, successCallback, failureCallback) {
            try {
                const usuarioId = sessionStorage.getItem('user_id');
                
                const [resTarefas, resEventos] = await Promise.all([
                    fetchAutenticado(`http://127.0.0.1:8080/api/tarefas?usuarioId=${usuarioId}`),
                    fetchAutenticado(`http://127.0.0.1:8080/api/eventos?usuarioId=${usuarioId}`)
                ]);

                const tarefas = await resTarefas.json();
                const eventos = await resEventos.json();

                const formatTarefas = tarefas.map(t => ({
                    title: "[T] " + t.titulo,
                    start: t.dataTarefa,
                    backgroundColor: t.statusTarefa === 'Concluida' ? '#84bfae' : '#5ce1e6',
                    extendedProps: { dbId: t.id } // Guarda o ID do banco para o clique
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

    currentCalendar = calendar;

    document.getElementById('formCadastro').onsubmit = async function(e) {
        e.preventDefault();
        if (!currentSelectionInfo) return;

        const tipo = document.getElementById('modalTipo').value;
        const title = document.getElementById('modalTitulo').value;
        const descricao = document.getElementById('modalDescricao').value;
        const prioridade = document.getElementById('modalPrioridade').value;
        const usuarioId = sessionStorage.getItem('user_id') || "6";
        const info = currentSelectionInfo;

        let url = "";
        let corpo = {};

        if (tipo === 'T') {
            url = 'http://127.0.0.1:8080/api/tarefas';
            corpo = {
                titulo: title,
                descricao: descricao,
                dataTarefa: info.startStr,
                prioridade: prioridade,
                statusTarefa: "Pendente",
                usuarioId: usuarioId.toString()
            };
        } else {
            url = 'http://127.0.0.1:8080/api/eventos';
            const dataEvento = info.startStr.includes("T") ? info.startStr : info.startStr + "T00:00:00";
            corpo = {
                titulo: title,
                descricao: descricao,
                dataEvento: dataEvento,
                usuarioId: usuarioId.toString()
            };
        }

        try {
            const response = await fetchAutenticado(url, {
                method: 'POST',
                body: JSON.stringify(corpo)
            });

            if (response.ok) {
                // Força o calendário e o gráfico a atualizarem com dados reais do banco
                calendar.refetchEvents();
                loadDashboardChart();
                fecharModal();
            } else {
                alert('Erro ao salvar no servidor.');
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
        }
    };

    calendar.render();
}

document.addEventListener('DOMContentLoaded', () => {
    displayUserInfo();
    loadDashboardChart();
    initCalendar();
});