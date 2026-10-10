const form = document.getElementById("fraud-form");
const result = document.getElementById("result");
const historyBody = document.getElementById("history");

let transactions = [];
let nextId = 1;

form.addEventListener("submit", function (event) {
  event.preventDefault();

  const amount = Number(document.getElementById("amount").value);
  const hour = Number(document.getElementById("hour").value);
  const failed = Number(document.getElementById("failed").value);
  const distance = Number(document.getElementById("distance").value);

  const device = document.getElementById("device").value;
  const location = document.getElementById("location").value;
  const international =
    document.getElementById("international").value;

  let score = 0;
  const indicators = [];

  if (amount >= 50000) {
    score += 20;
    indicators.push("Large transaction amount");
  }

  if (hour < 6 || hour > 22) {
    score += 10;
    indicators.push("Unusual transaction time");
  }

  if (failed >= 3) {
    score += 15;
    indicators.push("Multiple failed login attempts");
  }

  if (device === "yes") {
    score += 10;
    indicators.push("New device");
  }

  if (location === "yes") {
    score += 10;
    indicators.push("New location");
  }

  if (distance >= 500) {
    score += 15;
    indicators.push("Large travel distance");
  }

  if (international === "yes") {
    score += 10;
    indicators.push("International transaction");
  }

  let level;
  let action;

  if (score >= 50) {
    level = "HIGH";
    action = "BLOCK TRANSACTION";
  } else if (score >= 25) {
    level = "MEDIUM";
    action = "REQUEST EXTRA VERIFICATION";
  } else {
    level = "LOW";
    action = "ALLOW TRANSACTION";
  }

  document.getElementById("score").textContent = score;
  document.getElementById("level").textContent = level;
  document.getElementById("action").textContent = action;

  document.getElementById("indicators").textContent =
    "Risk indicators: " +
    (indicators.length ? indicators.join(", ") : "No risk indicators detected.");

  const transaction = {
    id: nextId++,
    amount: amount,
    score: score,
    level: level,
    time: new Date().toLocaleString()
  };

  transactions.unshift(transaction);
  transactions = transactions.slice(0, 10);

  historyBody.replaceChildren();

  transactions.forEach(function (t) {
    const row = document.createElement("tr");

    [t.id, "₹" + t.amount.toLocaleString("en-IN"),
      t.score, t.level, t.time].forEach(function (value) {
      const cell = document.createElement("td");
      cell.textContent = value;
      row.appendChild(cell);
    });

    historyBody.appendChild(row);
  });

  result.hidden = false;
});

document.getElementById("reset").addEventListener("click", function () {
  form.reset();
  result.hidden = true;
  form.scrollIntoView({ behavior: "smooth", block: "start" });
});