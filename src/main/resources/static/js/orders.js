$(function(){
  const token  = $('meta[name="_csrf"]').attr('content');
  const header = $('meta[name="_csrf_header"]').attr('content');
//test
  // 1) 메타데이터 호출
  $.ajax({
    url: '/api/orders/metadata',
    beforeSend: xhr => xhr.setRequestHeader(header, token),
    success: function(meta){
      // 2) 필터 초기화
      meta.statuses.forEach(s =>
        $('#statusFilter').append(
          `<option value="${s.code}">${s.name}</option>`
        )
      );
      // 3) 실제 주문 목록 호출
      $.ajax({
        url: '/api/orders',
        beforeSend: xhr => xhr.setRequestHeader(header, token),
        success: renderOrders,
        error: ()=> $('#orderContainer').text('목록 로드 실패')
      });
    },
    error: ()=> $('#orderContainer').text('초기값 로드 실패')
  });
});

function renderOrders(list){
  $('#orderContainer').html(
    list.map(o => `<p>Order#${o.id}: ${o.status}: ${o.amount} : ${o.orderDate}</p>`).join('')
  );
}
