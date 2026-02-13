const API_BASE = "http://localhost:8080";

const el = (id) => document.getElementById(id);

const apiStatus = el("apiStatus");
const apiBaseText = el("apiBaseText");
const logEl = el("log");

const artistForm = el("artistForm");
const artworkForm = el("artworkForm");
const updateForm = el("updateForm");

const btnRefresh = el("btnRefresh");
const btnDelete = el("btnDelete");
const btnReloadArtists = el("btnReloadArtists");

const listEl = el("list");
const searchEl = el("search");
const filterTypeEl = el("filterType");

const artworkTypeEl = el("artworkType");
const paintingFields = el("paintingFields");
const sculptureFields = el("sculptureFields");

const artistSelect = el("artistSelect");
const artistsList = el("artistsList");

const toastsEl = el("toasts");

apiBaseText.textContent = API_BASE;

// ===== Toasts =====
function toast(message, type = "info") {
    const t = document.createElement("div");
    t.className = `toast toast--${type}`;
    t.innerHTML = `
    <div class="toast__title">${type === "ok" ? "Готово" : type === "error" ? "Ошибка" : "Инфо"}</div>
    <div class="toast__msg">${escapeHtml(message)}</div>
  `;
    toastsEl.appendChild(t);

    requestAnimationFrame(() => t.classList.add("toast--show"));

    setTimeout(() => {
        t.classList.remove("toast--show");
        setTimeout(() => t.remove(), 250);
    }, 3500);
}

function escapeHtml(s) {
    return String(s ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;");
}

//  Logs
function log(line) {
    const time = new Date().toLocaleTimeString();
    logEl.textContent = `[${time}] ${line}\n\n` + logEl.textContent;
}

//  HTTP helper
async function apiFetch(path, options = {}) {
    const url = API_BASE + path;
    const res = await fetch(url, {
        headers: { "Content-Type": "application/json" },
        ...options,
    });

    const text = await res.text();
    let data;
    try { data = text ? JSON.parse(text) : null; } catch { data = text; }

    if (!res.ok) {
        const msg = (data && data.message) ? data.message : `HTTP ${res.status}`;
        throw { status: res.status, data, message: msg };
    }
    return data;
}

//  API status
async function checkApi() {
    try {
        await apiFetch("/artworks");
        apiStatus.textContent = "API: работает";
        apiStatus.classList.add("pill--ok");
        apiStatus.classList.remove("pill--bad");
    } catch (e) {
        apiStatus.textContent = "API: не отвечает";
        apiStatus.classList.add("pill--bad");
        apiStatus.classList.remove("pill--ok");
    }
}

//  Artists
async function loadArtists(selectIdToUse = null) {
    try {
        const artists = await apiFetch("/artists");
        renderArtists(artists);

        // fill select
        artistSelect.innerHTML = "";
        if (!artists.length) {
            const opt = document.createElement("option");
            opt.value = "";
            opt.textContent = "Сначала создай артиста";
            artistSelect.appendChild(opt);
            artistSelect.disabled = true;
            return;
        }

        artistSelect.disabled = false;

        for (const a of artists) {
            const opt = document.createElement("option");
            opt.value = a.id;
            opt.textContent = `#${a.id} — ${a.name} (${a.yearOfBirth}, ${a.nationality})`;
            artistSelect.appendChild(opt);
        }

        if (selectIdToUse) {
            artistSelect.value = String(selectIdToUse);
        }

    } catch (e) {
        log(`Ошибка GET /artists: ${JSON.stringify(e, null, 2)}`);
        toast("Не получилось загрузить artists. Проверь что сервер запущен.", "error");
    }
}

function renderArtists(artists) {
    if (!artists.length) {
        artistsList.innerHTML = `<div class="mini-item muted">Пока пусто</div>`;
        return;
    }

    artistsList.innerHTML = artists
        .slice(-8)
        .reverse()
        .map(a => `
      <div class="mini-item">
        <div class="mini-item__title">${escapeHtml(a.name)}</div>
        <div class="mini-item__meta">ID: <code>${a.id}</code> · ${a.yearOfBirth} · ${escapeHtml(a.nationality)}</div>
      </div>
    `)
        .join("");
}

//  Artworks
let cachedArtworks = [];

async function loadArtworks() {
    try {
        const artworks = await apiFetch("/artworks");
        cachedArtworks = artworks || [];
        renderArtworks();
        log(`Показано artworks: ${filteredArtworks().length}/${cachedArtworks.length}`);
    } catch (e) {
        log(`Ошибка GET /artworks: ${JSON.stringify(e, null, 2)}`);
        toast("Не получилось загрузить artworks.", "error");
    }
}

function filteredArtworks() {
    const q = (searchEl.value || "").trim().toLowerCase();
    const t = filterTypeEl.value;

    return cachedArtworks.filter(a => {
        const okTitle = !q || (a.title || "").toLowerCase().includes(q);
        const okType = !t || a.type === t;
        return okTitle && okType;
    });
}

function renderArtworks() {
    const arr = filteredArtworks();
    if (!arr.length) {
        listEl.innerHTML = `<div class="empty muted">Ничего не найдено</div>`;
        return;
    }

    listEl.innerHTML = arr.map(a => {
        const soldBadge = a.sold
            ? `<span class="badge badge--ok">Sold</span>`
            : `<span class="badge badge--no">Not sold</span>`;

        const detail = a.type === "Painting"
            ? `<div class="item__meta">Material: <code>${escapeHtml(a.material || "-")}</code> · Style: <code>${escapeHtml(a.style || "-")}</code></div>`
            : a.type === "Sculpture"
                ? `<div class="item__meta">Medium: <code>${escapeHtml(a.medium || "-")}</code> · Weight: <code>${a.weightKg ?? "-"}</code></div>`
                : "";

        return `
      <button class="item" data-id="${a.id}" type="button">
        <div class="item__top">
          <div>
            <h3 class="item__title">${escapeHtml(a.title)}</h3>
            <div class="item__meta">
              ID: <code>${a.id}</code> · Year: <code>${a.year}</code> · Price: <code>${a.price}</code>
            </div>
            <div class="item__meta">
              Artist: <code>${a.artistId ?? "-"}</code> · ${escapeHtml(a.artistName || "")}
            </div>
            ${detail}
          </div>
          <div class="stack">
            <span class="badge">${escapeHtml(a.type)}</span>
            ${soldBadge}
          </div>
        </div>
        <div class="item__hint">Клик → выбрать ID для Update/Delete</div>
      </button>
    `;
    }).join("");

    listEl.querySelectorAll(".item").forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.getAttribute("data-id");
            updateForm.elements.id.value = id;
            toast(`Выбран artwork ID: ${id}`, "info");
        });
    });
}

// UI toggles
function toggleTypeFields() {
    const type = artworkTypeEl.value;
    if (type === "Painting") {
        paintingFields.classList.remove("hidden");
        sculptureFields.classList.add("hidden");
    } else {
        paintingFields.classList.add("hidden");
        sculptureFields.classList.remove("hidden");
    }
}

artworkTypeEl.addEventListener("change", toggleTypeFields);

// Form handlers
artistForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const fd = new FormData(artistForm);

    const payload = {
        name: fd.get("name"),
        yearOfBirth: Number(fd.get("yearOfBirth")),
        nationality: fd.get("nationality"),
    };

    try {
        const res = await apiFetch("/artists", { method: "POST", body: JSON.stringify(payload) });
        log(`Создан artist\n${JSON.stringify(res, null, 2)}`);
        toast(`Артист создан. ID = ${res.id}`, "ok");

        artistForm.reset();
        await loadArtists(res.id); // auto select new artist
    } catch (e2) {
        log(`Ошибка POST /artists\n${JSON.stringify(e2, null, 2)}`);
        toast(e2.message || "Ошибка создания артиста", "error");
    }
});

artworkForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const fd = new FormData(artworkForm);

    const type = fd.get("type");
    const payload = {
        artistId: Number(fd.get("artistId")),
        type,
        title: fd.get("title"),
        year: Number(fd.get("year")),
        price: Number(fd.get("price")),
        sold: fd.get("sold") === "on",
    };

    // type-specific
    if (type === "Painting") {
        payload.material = fd.get("material") || "";
        payload.style = fd.get("style") || "";
        payload.medium = "";
        payload.weightKg = null;
    } else {
        payload.medium = fd.get("medium") || "";
        payload.weightKg = fd.get("weightKg") ? Number(fd.get("weightKg")) : null;
        payload.material = "";
        payload.style = "";
    }

    try {
        const res = await apiFetch("/artworks", { method: "POST", body: JSON.stringify(payload) });
        log(`Создан artwork\n${JSON.stringify(res, null, 2)}`);
        toast(`Artwork создан. ID = ${res.id}`, "ok");

        artworkForm.reset();
        toggleTypeFields();
        await loadArtworks();
    } catch (e2) {
        log(`Ошибка POST /artworks\n${JSON.stringify(e2, null, 2)}`);
        toast(e2.message || "Ошибка создания artwork", "error");
    }
});

updateForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const fd = new FormData(updateForm);

    const id = Number(fd.get("id"));
    const priceRaw = fd.get("price");
    const soldState = fd.get("soldState"); // "", "true", "false"

    const body = {};
    if (priceRaw !== null && String(priceRaw).trim() !== "") body.price = Number(priceRaw);
    if (soldState === "true") body.sold = true;
    if (soldState === "false") body.sold = false;

    if (Object.keys(body).length === 0) {
        toast("Нечего обновлять: введи price или выбери sold status", "info");
        return;
    }

    try {
        const res = await apiFetch(`/artworks/${id}`, { method: "PUT", body: JSON.stringify(body) });
        log(`Обновлено artwork ${id}\n${JSON.stringify(res, null, 2)}`);
        toast(`Artwork #${id} обновлён`, "ok");
        await loadArtworks();
    } catch (e2) {
        log(`Ошибка PUT /artworks/${id}\n${JSON.stringify(e2, null, 2)}`);
        toast(e2.message || "Ошибка обновления", "error");
    }
});

btnDelete.addEventListener("click", async () => {
    const id = Number(updateForm.elements.id.value);
    if (!id) {
        toast("Сначала выбери ID", "info");
        return;
    }

    if (!confirm(`Удалить artwork #${id}?`)) return;

    try {
        const res = await apiFetch(`/artworks/${id}`, { method: "DELETE" });
        log(`Удалено artwork ${id}\n${JSON.stringify(res, null, 2)}`);
        toast(`Artwork #${id} удалён`, "ok");
        await loadArtworks();
    } catch (e2) {
        log(`Ошибка DELETE /artworks/${id}\n${JSON.stringify(e2, null, 2)}`);
        toast(e2.message || "Ошибка удаления", "error");
    }
});

// Buttons / filters
btnRefresh.addEventListener("click", async () => {
    await checkApi();
    await loadArtists();
    await loadArtworks();
    toast("Обновлено", "ok");
});

btnReloadArtists.addEventListener("click", async () => {
    await loadArtists();
    toast("Artists обновлены", "ok");
});

searchEl.addEventListener("input", renderArtworks);
filterTypeEl.addEventListener("change", renderArtworks);

// init
(async function init() {
    toggleTypeFields();
    await checkApi();
    await loadArtists();
    await loadArtworks();
})();
