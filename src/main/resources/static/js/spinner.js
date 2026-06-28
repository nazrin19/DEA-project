/**
 * spinner.js
 * ---------------------------------------------------------------
 * Loading-state helpers shared by every page in Lankatools.
 *
 * 1. Full-page overlay
 *      window.showLoading() / window.hideLoading()
 *
 * 2. Automatic spinner on EVERY fetch() call made anywhere in the
 *    app — pages don't have to do anything extra for plain fetch()
 *    calls to API endpoints (e.g. /api/tools, /api/bookings).
 *    To opt a specific call out, pass { skipSpinner: true } as
 *    part of the fetch init object.
 *
 * 3. Button-level spinner for form submits, so the user gets
 *    feedback right on the button they clicked instead of (or in
 *    addition to) the full overlay.
 * ---------------------------------------------------------------
 */
(function () {
    'use strict';

    var activeRequests = 0;
    var overlay = null;

    function getOverlay() {
        if (!overlay) {
            overlay = document.getElementById('global-loading-overlay');
        }
        return overlay;
    }

    function showLoading() {
        var el = getOverlay();
        if (el) el.classList.add('active');
    }

    function hideLoading() {
        var el = getOverlay();
        if (el) el.classList.remove('active');
    }

    var originalFetch = window.fetch;
    window.fetch = function (input, init) {
        var skip = init && init.skipSpinner;

        if (!skip) {
            activeRequests++;
            showLoading();
        }

        return originalFetch(input, init)
            .then(function (response) {
                if (!skip) {
                    activeRequests--;
                    if (activeRequests <= 0) {
                        activeRequests = 0;
                        hideLoading();
                    }
                }
                return response;
            })
            .catch(function (err) {
                if (!skip) {
                    activeRequests--;
                    if (activeRequests <= 0) {
                        activeRequests = 0;
                        hideLoading();
                    }
                }
                throw err;
            });
    };

    function setButtonLoading(button, isLoading) {
        if (!button) return;
        if (isLoading) {
            button.dataset.wasDisabled = button.disabled ? '1' : '0';
            button.disabled = true;
            button.classList.add('is-loading');
        } else {
            button.disabled = button.dataset.wasDisabled === '1';
            button.classList.remove('is-loading');
        }
    }

    document.addEventListener('submit', function (e) {
        var form = e.target;
        if (form && form.tagName === 'FORM' && !form.hasAttribute('data-no-spinner')) {
            showLoading();
            var submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) setButtonLoading(submitBtn, true);
        }
    });

    window.showLoading = showLoading;
    window.hideLoading = hideLoading;
    window.Lankatools = window.Lankatools || {};
    window.Lankatools.setButtonLoading = setButtonLoading;
})();
