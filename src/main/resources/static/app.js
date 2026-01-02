async function postJson(url, payload) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  const txt = await res.text();
  let obj;
  try { obj = JSON.parse(txt); } catch { obj = { raw: txt }; }
  if (!res.ok) throw new Error((obj && obj.detail) ? obj.detail : ("HTTP " + res.status));
  return obj;
}

function basePayload() {
  return {
    modelId: document.getElementById("modelId").value.trim(),
    incidentType: document.getElementById("incidentType").value,
    metricName: document.getElementById("metricName").value.trim(),
    value: Number(document.getElementById("value").value),
    threshold: Number(document.getElementById("threshold").value),
    notes: document.getElementById("notes").value.trim()
  };
}

function setOut(obj) {
  document.getElementById("out").textContent = JSON.stringify(obj, null, 2);
}

async function trigger(severity) {
  const payload = basePayload();
  payload.forceSeverity = severity; // demo only
  setOut({ sending: payload });
  try {
    const r = await postJson("/api/mlops/event", payload);
    setOut(r);
  } catch (e) {
    setOut({ error: String(e) });
  }
}

document.getElementById("btnWarn").addEventListener("click", () => trigger("WARN"));
document.getElementById("btnAlert").addEventListener("click", () => trigger("ALERT"));
document.getElementById("btnCritical").addEventListener("click", () => trigger("CRITICAL"));

(async () => {
  try {
    const res = await fetch("/health");
    if (res.ok) setOut({ ready: true, health: await res.json() });
  } catch {}
})();
