$(function() {
	const form = $('#upload-form')
		, uploadBtn = $('#upload-btn');
	uploadBtn.click(function() {
		$.ajax({
			url: form.attr('action')
			, type: 'POST'
			, data: new FormData($('#upload-form')[0])
			, processData: false
			, contentType: false
		}).done(function(data) {
			let i = 0;
		}).fail(function(e) {
			let i = 0;
		});
	});
});
