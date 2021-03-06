(function() {
    'use strict';

    angular
        .module('inthemomentbotApp')
        .controller('SettingDetailController', SettingDetailController);

    SettingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Setting'];

    function SettingDetailController($scope, $rootScope, $stateParams, previousState, entity, Setting) {
        var vm = this;

        vm.setting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('inthemomentbotApp:settingUpdate', function(event, result) {
            vm.setting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
