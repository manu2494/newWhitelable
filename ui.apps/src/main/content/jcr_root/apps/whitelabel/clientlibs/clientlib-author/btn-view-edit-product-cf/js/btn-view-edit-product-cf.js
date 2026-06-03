// Handle on for dialog comp
$(document).on("dialog-ready", bindViewEditProductCF);

// Handle for Page Properties
$(document).ready(bindViewEditProductCF);

function bindViewEditProductCF() {
  let btnOpenProductCf = $('.danone-btn-open-cf');

  if (btnOpenProductCf.length > 0) {
    $(btnOpenProductCf[0]).click(function() {
      let productCfPathSetting = $("input[name='./productMaster']");
      if(!productCfPathSetting || productCfPathSetting.length === 0){
        productCfPathSetting = $("input[name='./productCfPath']");
      }

      if (productCfPathSetting && productCfPathSetting.val()) {
        window.open('/editor.html' + productCfPathSetting.val(), '_blank');
      }
    });

  }
}