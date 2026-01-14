function calculate() {
    let num1 = document.getElementById("num1").value;
    let num2 = document.getElementById("num2").value;
    let op = document.getElementById("operation").value;

    console.log("Inputs:", num1, num2, op);

    fetch(`http://localhost:9090/calc?num1=${num1}&num2=${num2}&op=${op}`)
        .then(response => response.text())
        .then(data => {
            console.log("Response from backend:", data);
            document.getElementById("result").innerText = "Result: " + data;
        })
        .catch(error => {
            console.error("Fetch error:", error);
            document.getElementById("result").innerText = "Backend not reachable";
        });
}
