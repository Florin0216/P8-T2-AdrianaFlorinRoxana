const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    stompClient.subscribe('/user/queue/conference-notifications', function (message) {
        const payload = JSON.parse(message.body);
        showPopup(payload.title, payload.joinUrl);
    });
});


function showPopup(title, joinUrl) {
    const fullMessage = `The conference is starting now!\nReason: ${title}\nRedirecting now!`;
    alert(fullMessage);
    window.location.href = joinUrl;
}
